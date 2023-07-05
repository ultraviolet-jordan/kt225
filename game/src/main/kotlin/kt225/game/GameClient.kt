package kt225.game

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kt225.common.buffer.flip
import kt225.common.buffer.p1
import kt225.common.buffer.p2
import kt225.common.buffer.position
import kt225.common.crypto.IsaacRandom
import kt225.common.game.Client
import kt225.common.network.Session
import kt225.common.packet.Packet
import kt225.common.packet.PacketGroup
import java.nio.ByteBuffer
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.ConcurrentHashMap

/**
 * @author Jordan Abraham
 */
class GameClient(
    serverSeed: IntArray,
    clientSeed: IntArray,
    private val session: Session
) : Client(IsaacRandom.create(serverSeed), IsaacRandom.create(clientSeed)) {
    private val readChannelQueue = ConcurrentHashMap<Int, ArrayBlockingQueue<PacketGroup>>()
    private val writeChannelQueue = ByteBuffer.allocateDirect(5000)

    override fun writePacket(packet: Packet) {
        write(writeChannelQueue, packet)
    }

    override fun readPacket(packet: Packet) {
        val handler = session.handlers[packet::class] ?: return
        val group = PacketGroup(packet, handler)
        readChannelQueue
            .computeIfAbsent(group.handler.groupId) { ArrayBlockingQueue<PacketGroup>(10) }
            .offer(group)
    }

    override fun flushWriteQueue() {
        flush(writeChannelQueue)
        writeChannelQueue.clear()
    }

    override fun consumeReadQueue() {
        for (handler in readChannelQueue) {
            val queue = handler.value
            for (i in 0 until 10) {
                val group = queue.poll() ?: break
                group.handler.handlePacket(group.packet, this)
            }
            queue.clear()
        }
    }

    override fun writePacketDirect(packet: Packet, length: Int) {
        val buffer = ByteBuffer.allocate(length + 3)
        write(buffer, packet)
        flush(buffer)
    }

    private fun write(buffer: ByteBuffer, packet: Packet) {
        val builder = session.builders[packet::class] ?: return
        buffer.p1(builder.id + serverIsaac.nextInt)
        if (builder.length != -1 && builder.length != -2) {
            builder.buildPacket(packet, buffer)
            return
        }
        val startPos = buffer.position
        val offset = startPos + if (builder.length == -1) 1 else 2
        buffer.position(offset)
        builder.buildPacket(packet, buffer)
        val endPos = buffer.position
        val size = endPos - offset
        buffer.position(startPos)
        when (builder.length) {
            -1 -> buffer.p1(size)
            -2 -> buffer.p2(size)
            else -> throw IllegalStateException("Builder length can only be -1 or -2 here.")
        }
        buffer.position(endPos)
    }

    private fun flush(buffer: ByteBuffer) {
        val writeChannel = session.writeChannel
        if (writeChannel.isClosedForWrite) return
        runBlocking(Dispatchers.IO) {
            writeChannel.writeFully(buffer.flip)
        }
        writeChannel.flush()
    }
}
