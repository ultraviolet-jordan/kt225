package kt225.game

import com.runetopic.cryptography.toISAAC
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kt225.common.buffer.p1
import kt225.common.buffer.p2
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
) : Client(
    serverIsaac = serverSeed.toISAAC(),
    clientIsaac = clientSeed.toISAAC()
) {
    private val readChannelQueue = ConcurrentHashMap<Int, ArrayBlockingQueue<PacketGroup>>()
    private val writeChannelQueue = ByteBuffer.allocateDirect(100_000)

    override fun writePacket(packet: Packet) {
        val builder = session.builders[packet::class] ?: return

        writeChannelQueue.p1(builder.id + serverIsaac.getNext() and 0xFF)

        if (builder.length != -1 && builder.length != -2) {
            builder.buildPacket(packet, writeChannelQueue)
            return
        }

        val startPos = writeChannelQueue.position()
        val offset = startPos + if (builder.length == -1) 1 else 2
        writeChannelQueue.position(offset)
        builder.buildPacket(packet, writeChannelQueue)
        val endPos = writeChannelQueue.position()
        val size = endPos - offset
        writeChannelQueue.position(startPos)
        if (builder.length == -1) {
            writeChannelQueue.p1(size)
        } else {
            writeChannelQueue.p2(size)
        }
        writeChannelQueue.position(endPos)
    }

    override fun flushWriteQueue() {
        val writeChannel = session.writeChannel
        if (writeChannel.isClosedForWrite) return
        // This way we only have to suspend once per client.
        runBlocking(Dispatchers.IO) {
            writeChannel.writeFully(writeChannelQueue.flip())
        }
        writeChannel.flush()
        writeChannelQueue.clear()
    }

    override fun flushReadQueue() {
        TODO("Not yet implemented")
    }
}
