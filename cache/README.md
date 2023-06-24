# Cache

The RuneScape cache is a vital component of the popular MMORPG 
(Massively Multiplayer Online Role-Playing Game) RuneScape. It contains various game assets, 
such as media, sounds, maps, and other resources necessary for the game to function. However, 
the RuneScape cache is stored in a proprietary format, which means it is not openly documented 
and understood.

## Proprietary Format
Jagex, the developer of RuneScape, has chosen to use a proprietary format
for the RuneScape cache to protect their game's assets and prevent unauthorized
access or modification. This format is designed to be difficult to decipher without
the necessary tools and knowledge.

The specifics of the proprietary format used in the RuneScape cache are not
publicly disclosed by Jagex. Reverse engineering or attempting to extract the
data directly from the cache can be a complex and challenging task. This deliberate
obfuscation aims to deter cheating, hacking, or any other form of unauthorized
manipulation of the game.

- **Jag File** (Raw Input File From Directory)
  - `u24` (Decompressed length)
  - `u24` (Compressed length)
    - `if (Decompressed length != Compressed length)`
      - `Decompress with Bzip2 with length of "Compressed length" at the current read pointer`
        - **Even though it uses Bzip2, this does not contain a Bzip2 header. ("BZh1")**
  - `u16` (Number of entries)
  - **Entries** (Array)
    - `var offset = 8 + "Number of entries" * 10`
    - `u32` (Individual entry hashed name)
    - `u24` (Individual entry decompressed length)
    - `u24` (Individual entry compressed length)
    - `val pointer = "Mark the current read position pointer"`
        - `if (Decompressed length != Compressed length)`
            - `Decompress with Bzip2 starting at position "offset" with length of "Individual entry compressed length"`
              - **Even though it uses Bzip2, this does not contain a Bzip2 header. ("BZh1")**
        - `else continue reading bytes with the length of "Individual entry decompressed length"`
    - `val bytes = "Decompressed bytes or not decompressed bytes from condition"`
    - `Reset the read position pointer to the marked "pointer" above`
    - `offset += "Individual entry compressed length"`
      - **Entry**

## Decompilation and Extraction
While the RuneScape cache is in a proprietary format, the community has made
considerable efforts to reverse engineer and understand its structure. Various
tools and utilities have been developed by dedicated individuals to extract
specific assets from the cache for analysis or personal use. This module provides
the ability to programmatically read and write contents of the cache files.

## Compression & Decompression
When it comes to the RuneScape cache, either the entire archive or individual files
are compressed using the **original Bzip2 algorithm**. This compression is applied to reduce the
overall file size, optimize storage requirements, and potentially improve data 
transfer speeds during game updates or downloads.

This module takes advantage of the Apache commons Bzip2 dependencies for handling
the compression and decompression. However, for the algorithm to match the original
RuneScape implementation bit by bit, the fix was done 
by [OpenRS2](https://github.com/openrs2/openrs2) instead of using the pure Java implementation.

### Usage
_All of the following examples use the "config" archive as an example archive.
Please keep in mind there are multiple archives used by the game._

_You can also look at the unit tests for more in depth examples._

#### Reading and Writing a Jag cache archive.
```kotlin
val bytes = File("config").readBytes() // may or may not have .jag extension.
val configArchive = ConfigArchive(bytes)
val zipped = configArchive.pack()
val unzipped = ConfigArchive(zipped)

// Will produce the following crc.
assertEquals(configArchive.crc, unzipped.crc)
assertEquals(511217062, configArchive.crc)
```

#### Reading a file from an archive.
```kotlin
// Generates a ByteBuffer backed by the specified file bytes.
val objFileBuffer = configArchive.read("obj.dat")
```

#### Getting a file from an archive.
```kotlin
// Returns a possible file with the specified file name.
val objFile = configArchive.file("obj.dat")
```

#### Adding a file to an archive.

```kotlin
val fileBuffer = ByteBuffer.allocate(100)
// Encode the entire file to this buffer.
fileBuffer.flip()
val added = configArchive.add("obj.dat", fileBuffer)

// Example code at the end to save the config archive with the changes to obj.dat file.
val zipped = configArchive.pack()
val file = File("config")
file.writeBytes(zipped)
```

#### Removing a file from an archive.

```kotlin
val removed = configArchive.remove("obj.dat")

// Example code at the end to save the config archive with the changes to remove the obj.dat file.
val zipped = configArchive.pack()
val file = File("config")
file.writeBytes(zipped)
```

#### Miscellaneous properties of an archive.

```kotlin
// Returns the crc of the raw jag file bytes.
val crc = configArchive.crc
// Returns the last entry of this jag file.
val last = configArchive.last
// Returns all the entries of this jag file.
val view = configArchive.view
```

## Guice
This module is built with Guice. If your project also uses Guice, then you can install
the entire module and Inject the archives you want.

```kotlin
install(CacheModule)
// or
val injector = Guice.createInjector(CacheModule)

class Test @Inject constructor(
    private val configArchive: ConfigArchive,
    private val mediaArchive: MediaArchive,
    private val mapLands: MapLands,
    private val mapLocs: MapLocs
)
```