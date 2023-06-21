# Cache-225

This code module provides functionality for reading and writing specific files within
the RuneScape cache files, using the proprietary file format employed by the game. 
The module is tailored specifically for the 225 revision of RuneScape, allowing proper
handling of files within the Jag archive.

## Overview
The cache files in RuneScape undergo changes between game versions due to content 
additions and engine updates. Each game revision introduces new content, necessitating
modifications to the cache file format to accommodate the additional data.

The module's purpose is to read and write specific files within the Jag archive for the 
225 revision of RuneScape. It understands the structure of the cache files and performs
the necessary operations to manipulate them.

## Features
The module includes the following features:

1. **Parsing the cache file headers:** Extracts information about the files stored in the cache, such as their names, sizes, and locations within the archive.
2. **Decompression and decoding:** Handles the decompression or decoding of cache files as required, to retrieve the original content.
3. **File extraction and modification:** Provides methods to extract specific files from the cache, allowing access to their content for reading or modification.
4. **File creation and insertion:** Supports creating new cache files and inserting them into the appropriate locations within the cache.

## Current Support
- `config`
  - `obj`
  - `varp`
- `lands ("m")`
- `locs ("l")`

## Usage
_You can also look at the unit tests for more in depth examples._

#### Reading, Modifying and Writing a Specific File
```kotlin
val bytes = File("config").readBytes() // may or may not have .jag extension.
val original = JagArchive.decode(bytes)
val configArchive = ConfigArchive(original)
val objsProvider = ObjsProvider(configArchive)

// Reads the "obj.dat" file from this archive.
val objs = objsProvider.read()

val abyssalWhip = objs[4151]
assertEquals("Abyssal whip", abyssalWhip.name)
abyssalWhip?.name = "My new whip"

val purplePartyHat = objs[1046]
assertEquals("Purple partyhat", purplePartyHat.name)
purplePartyHat?.name = "My new partyhat"

val size = objs.size
// If you skip entries, the CacheModule will always encode the available files in order.
// So if your last id was 100, and you create a new obj with id 500, it will be encoded as id 101.
val newObj = ObjEntryType(id = size + 1, name = "My new item")
objs[newObj.id] = newObj

objsProvider.write(objs)

val zipped = JagArchive.encode(configArchive)
val file = File("config")
file.writeBytes(zipped)
```

```kotlin
val mapLocs = MapLocs()
val mapSquareLocsProvider = MapSquareLocsProvider(mapLocs)
val mapSquareLocs = mapSquareLocsProvider.read()

val lumbridge = mapSquareLocs[12850]!!

lumbridge.query(Position(3223, 3220, 0)) { result, local ->
    val bush = result.firstOrNull { it.id == 1124 } ?: return@query
    val removed = lumbridge.removeLoc(bush, local)
    assertTrue(removed)
}

lumbridge.query(Position(3223, 3222, 0)) { _, local ->
    val chest = MapSquareLoc(2191, local.x, local.z, local.plane, 10, 0)
    val added = lumbridge.addLoc(chest, local)
    assertTrue(added)
}

mapSquareLocsProvider.write(mapSquareLocs)

val mapResource = mapLocs.first { it.id == 12850 }
val bytes = mapResource.bytes
val file = File("./${mapResource.name}")
file.writeBytes(bytes)
```

<p align="center">
  <img src="https://github.com/ultraviolet-jordan/kt225/blob/main/github/map_edit_example.jpg?raw=true" alt="An example of an edit to the map."/>
</p>

## Guice
This module is built with Guice. If your project also uses Guice, then you can install
the entire module and Inject the archives you want.

```kotlin
install(Cache225Module)
// or
val injector = Guice.createInjector(Cache225Module)

class Test @Inject constructor(
    private val objs: Objs<ObjEntryType>,
    private val objsProvider: ObjsProvider
)
```