Standard AuxData Schemata
=========================

The \ref AUXDATA_GROUP class provides generic storage for
application-specific data. This allows data to be attached to either
the IR or Module classes in GTIRB.

We specify a small number of standard gtirb::AuxData schemata to
support interoperability. These are listed below, in two sets:

- [Sanctioned](#sanctioned-auxdata-tables) Recommended for GTIRB
  users. Individual schemata are unlikely to change in future,
  although the set of Sanctioned schemata may grow.

- [Provisional](#provisional-auxdata-tables) Under consideration for
  'sanctioned' status.

For example, if you want to store alignment requirements for blocks
and data objects, you can use an [alignment](#alignment) table.

```c++
// Leverage definitions for the sanctioned AuxData tables.
#include <gtirb/AuxDataSchema.hpp>

// Define your own custom AuxData tables.
// By convention, we put these in the namespace gtirb::schema.
//
// Note that if the custom type requires custom serialization, a
// specialization of the auxdata_traits template also has to be
// provided. We provide default specializations for many standard
// types.
namespace gtirb {
namespace schema {
struct MyAuxDataFoo {
  static constexpr const char* Name = "foo";
  typedef Foo Type;
};
}
}

using namespace gtirb;
using namespace schema;

// Register AuxData types before using GTIRB.
void call_me_from_main()
{
  AuxDataContainer::registerAuxDataType<Alignment>();
  AuxDataContainer::registerAuxDataType<MyAuxDataFoo>();
}

void do_stuff_with_gtirb()
{
  Context C;
  IR& ir = *IR::Create(C);
  ir.addModule(Module::Create(C));
  Module& module = *ir.modules_begin();

  // Attach an empty alignment table to the internal representation
  module.addAuxData<Alignment>(std::map<UUID, uint64_t>{});

  //...

  // Create a new block
  Section* section = module.addSection(C, ".text");
  ByteInterval* interval = section->addByteInterval(C, Addr(400), 1000);
  CodeBlock* b1 = interval->addBlock<CodeBlock>(C, 64, 6);

  // Record that the block should be aligned to 8-byte boundaries.
  // First fetch the map AuxData.
  auto* align_map = module.getAuxData<Alignment>();

  // Check for null if you don't know that the module definitely has
  // an existing Alignment AuxData attached.
  if (align_map)
    (*align_map)[b1->getUUID()] = 8;

  // Attach a custom "Foo" object.
  // Note that AuxData uses a move reference
  Foo my_foo = BuildAFoo();
  module.addAuxData<MyAuxDataFoo>(std::move(my_foo));

  // Subsequently access the Foo table through the AuxData interface.
  module.getAuxData<MyAuxDataFoo>()->some_member_function();
}
```


## Sanctioned AuxData Tables

The following are the sanctioned AuxData table schemata.


| Label                                     | Type                                               |
|-------------------------------------------|----------------------------------------------------|
| [`"elfDynamicInit"`](#elfDynamicInit)     | ```gtirb::UUID```                                  |
| [`"elfDynamicFini"`](#elfDynamicFini)     | ```gtirb::UUID```                                  |
| [`"elfStackExec"`](#elfStackExec)         | ```bool```                                         |
| [`"elfStackSize"`](#elfStackSize)         | ```uint64_t```                                     |
| [`"functionBlocks"`](#functionblocks)     | ```std::map<gtirb::UUID, std::set<gtirb::UUID>>``` |
| [`"functionEntries"`](#functionentries)   | ```std::map<gtirb::UUID, std::set<gtirb::UUID>>``` |
| [`"functionNames"`](#functionnames)       | ```std::map<gtirb::UUID, gtirb::UUID>```           |
| [`"types"`](#types)                       | ```std::map<gtirb::UUID, std::string>```           |
| [`"alignment"`](#alignment)               | ```std::map<gtirb::UUID, uint64_t>```              |
| [`"comments"`](#comments)                 | ```std::map<gtirb::Offset, std::string>```         |
| [`"symbolForwarding"`](#symbolforwarding) | ```std::map<gtirb::UUID, gtirb::UUID>```           |
| [`"padding"`](#padding)                   | ```std::map<gtirb::Offset, uint64_t>```            |


### elfDynamicInit

| <!-- --> | <!-- -->                                           |
|----------|----------------------------------------------------|
| Label    | ```"elfDynamicInit"```                             |
| Type     | ```gtirb::UUID```                                  |
| Value    |  CodeBlock UUID                                    |
| AttachedTo | gtirb::Module |
| Note     | The CodeBlock to which a DT_INIT entry in an ELF file's .dynamic section refers. |

### elfDynamicFini

| <!-- --> | <!-- -->                                           |
|----------|----------------------------------------------------|
| Label    | ```"elfDynamicFini"```                             |
| Type     | ```gtirb::UUID```                                  |
| Value    |  CodeBlock UUID                                    |
| AttachedTo | gtirb::Module |
| Note     | The CodeBlock to which a DT_FINI entry in an ELF file's .dynamic section refers. |

### elfStackExec

| <!-- --> | <!-- -->                                           |
|----------|----------------------------------------------------|
| Label    | ```"elfStackExec"```                               |
| Type     | ```bool```                                         |
| Value    | Stack executable flag specified by PT_GNU_STACK segment in ELF files. |
| AttachedTo | gtirb::Module |

### elfStackSize

| <!-- --> | <!-- -->                                           |
|----------|----------------------------------------------------|
| Label    | ```"elfStackSize"```                               |
| Type     | ```uint64_t```                                     |
| Value    | The size of the PT_GNU_STACK segment in ELF files, which may influence the runtime stack size in certain environments. |
| AttachedTo | gtirb::Module |

### functionBlocks

| <!-- --> | <!-- -->                                           |
|----------|----------------------------------------------------|
| Label    | ```"functionBlocks"```                             |
| Type     | ```std::map<gtirb::UUID, std::set<gtirb::UUID>>``` |
| Key      | Function UUID.                                     |
| Value    | The set of UUIDs of all the blocks (gtirb::CodeBlock) in the function. |
| AttachedTo | gtirb::Module |
| Note     | This table identifies all of the gtirb::CodeBlocks that belong to each function. These do not necessarily have to be contiguous in the address space. Note that there is no function notion in the core GTIRB IR. A function's UUID is just a unique identifier that is consistently used across all function-related AuxData tables. |


### functionEntries

| <!-- --> | <!-- -->                                           |
|----------|----------------------------------------------------|
| Label    | ```"functionEntries"```                            |
| Type     | ```std::map<gtirb::UUID, std::set<gtirb::UUID>>``` |
| Key      | Function UUID.                                     |
| Value    | The set of UUIDs of all the entry blocks (gtirb::CodeBlock) for the function. |
| AttachedTo | gtirb::Module |
| Note     | This table identifies all gtirb::CodeBlocks that represent entry points to each function. A single function may have more than one entry point. Note that there is no function notion in the core GTIRB IR. A function's UUID is just a unique identifier that is consistently used across all function-related AuxData tables. |


### functionNames

| <!-- --> | <!-- -->                                                            |
|----------|---------------------------------------------------------------------|
| Label    | ```"functionNames"```                                               |
| Type     | ```std::map<gtirb::UUID, gtirb::UUID>```                            |
| Key      | Function UUID.                                                      |
| Value    | The UUID of a gtrb::Symbol whose `name` field contains the name of the function. |
| AttachedTo | gtirb::Module |
| Note     | There may be more than one gtirb::Symbol associated with the address(es) corresponding to the entry point(s) of a function. This table identifies a canonical gtirb::Symbol to be used for each function. Note that there is no function notion in the core GTIRB IR. A function's UUID is just a unique identifier that is consistently used across all function-related AuxData tables. |


### types

| <!-- --> | <!-- -->                                |
|----------|-----------------------------------------|
| Label    | ```"types"```                           |
| Type     | ```std::map<gtirb::UUID,std::string>``` |
| Key      | The gtirb::UUID of a gtirb::DataBlock. |
| Value    | The type of the data, expressed as a std::string containing a C++ type specifier. |
| AttachedTo | gtirb::Module |
| Note     | An entry in this table indicates that the given gtirb::DataBlock contains content that exhibits the given C++ type. |


### alignment

| <!-- --> | <!-- -->                                                  |
|----------|-----------------------------------------------------------|
| Label    | ```"alignment"```                                         |
| Type     | ```std::map<gtirb::UUID, uint64_t>```                     |
| Key      | The gtirb::UUID of a gtirb::CodeBlock, gtirb::DataBlock, or gtirb::Section. |
| Value    | Alignment requirements for the block/data object/section. |
| AttachedTo | gtirb::Module |
| Note     |  An entry in this table indicates that the given object's address is required to be evenly divisible by the alignment value. Typically the alignment value is a power of 2. |


### comments

| <!-- --> | <!-- -->                                   |
|----------|--------------------------------------------|
| Label    | ```"comments"```                           |
| Type     | ```std::map<gtirb::Offset, std::string>``` |
| Key      | The gtirb::Offset of a comment.            |
| Value    | A comment string relevant to the specified offset in the specified GTIRB entry. |
| AttachedTo | gtirb::Module |
| Note     | The gtirb::Offset refers to the UUID of an entity in memory and a byte offset within that entity to indicate the point at which the comment applies. Comments can contain arbitrary content and are likely generated by analysis tools. They often do not (but may) represent comments present in the original source code of the binary. |


### symbolForwarding

| <!-- --> | <!-- -->                                     |
|----------|----------------------------------------------|
| Label    | ```"symbolForwarding"```                     |
| Type     | ```std::map<gtirb::UUID,gtirb::UUID>```      |
| Key      | The gtirb::UUID of the "from" gtirb::Symbol. |
| Value    | The gtirb::UUID of the "to" gtirb::Symbol.   |
| AttachedTo | gtirb::Module |
| Note     | This table is intended to support cross-module references. A "from" symbol in one gtirb::Module may be dynamically bound at runtime to the "to" symbol in another gtirb::Module, thereby modeling dynamic library runtime linkage. |


### padding

| <!-- --> | <!-- -->                                       |
|----------|------------------------------------------------|
| Label    | ```"padding"```                                |
| Type     | ```std::map<gtirb::Offset, uint64_t>```        |
| Key      | The gtirb::Offset at which padding is present. |
| Value    | The length of the padding, in bytes.           |
| AttachedTo | gtirb::Module |
| Note     | Padding here may be 0's or it may be valid instructions. An entry in this table indicates that an analysis has determined that at the given gtirb::Offset (UUID of an entity in memory and byte offset into that entity) and length of bytes indicated constitute content that is unused by the program and is only present to ensure alignment of neighboring objects. Note: some disassemblers may still create a gtirb::CodeBlock or gtirb::DataBlock for the same portion of address space that a padding entry covers. |


## Provisional AuxData Tables

The following are the provisional AuxData table schemata.


| Label                                                       | Type                                                                                                         |
|-------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------|
| [`"binaryType"`](#binarytype)                               | ```std::vector<std::string>```                                                                               |
| [`"cfiDirectives"`](#cfidirectives)                         | ```std::map<gtirb::Offset, std::vector<std::tuple<std::string, std::vector<int64_t>, gtirb::UUID>>>```       |
| [`"elfSectionProperties"`](#elfsectionproperties)           | ```std::map<gtirb::UUID, std::tuple<uint64_t, uint64_t>>>>```                                                |
| [`"elfSymbolInfo"`](#elfsymbolinfo)                         | ```std::map<gtirb::UUID, std::tuple<uint64_t, std::string, std::string, std::string, uint64_t>>```           |
| [`"elfSymbolVersions"`](#elfsymbolversions)                 | ```std::tuple<std::map<uint16_t, std::tuple<std::vector<std::string>, uint16_t>>, std::map<std::string, std::map<uint16_t, std::string>>, std::map<gtirb::UUID, std::tuple<uint16_t, bool>>>```           |
| [`"encodings"`](#encodings)                                 | ```std::map<gtirb::UUID, std::string>```                                                                     |
| [`"functionNameProbabilities"`](#functionnameprobabilities) | ```std::map<std::string, std::map<gtirb::UUID, std::vector<std::tuple<std::string, std::string, float>>>>``` |
| [`"includedLibraryNames"`](#includedlibrarynames)           | ```std::map<gtirb::UUID, std::string>```                                                                     |
| [`"includedLibraryVersions"`](#includedlibraryversions)     | ```std::map<gtirb::UUID, std::string>```                                                                     |
| [`"libraries"`](#libraries)                                 | ```std::vector<std::string>```                                                                               |
| [`"libraryPaths"`](#librarypaths)                           | ```std::vector<std::string>```                                                                               |
| [`"peExportEntries"`](#peexportentries)                     | ```std::vector<std::tuple<uint64_t, int64_t, std::string>>```                                                |
| [`"peExportedSymbols"`](#peexportedsymbols)                 | ```std::vector<gtirb::UUID>```                                                                               |
| [`"peImportEntries"`](#peimportentries)                     | ```std::vector<std::tuple<uint64_t, int64_t, std::string, std::string>>```                                   |
| [`"peImportedSymbols"`](#peimportedsymbols)                 | ```std::vector<gtirb::UUID>```                                                                               |
| [`"peResource"`](#peresource)                               | ```std::vector<std::tuple<std::vector<uint8_t>, gtirb::Offset, uint64_t>>```                                 |
| [`"profile"`](#profile)                                     | ```std::map<gtirb::UUID, uint64_t>```                                                                        |
| [`"prototypeTable"`](#prototypetable)                       | ```std::map<gtirb::UUID, gtirb::UUID>```                                                                     |
| [`"sccs"`](#sccs)                                           | ```std::map<gtirb::UUID, int64_t>```                                                                         |
| [`"symbolicExpressionSizes"`](#symbolicexpressionsizes)     | ```std::map<gtirb::Offset, uint64_t>```                                                                      |
| [`"typeTable"`](#typetable)                                 | ```std::map<gtirb::UUID, std::variant<uint64_t, std::tuple<uint8_t>, std::tuple<int8_t, uint64_t>, uint64_t, uint64_t, std::tuple<gtirb::UUID, std::vector<gtirb::UUID>>, gtirb::UUID, std::tuple<gtirb::UUID, uint64_t>, tuple<uint64_t, std::vector<tuple<uint64_t, gtirb::UUID>>>, std::tuple<uint8_t>, gtirb::UUID>>``` |


### encodings

| <!-- --> | <!-- -->                                       |
|----------|------------------------------------------------|
| Label    | ```"encodings"```                                |
| Type     | ```std::map<gtirb::UUID, std::string>```        |
| Key      | The gtirb::UUID of a data object. |
| Value    | The encoding of the data object.           |
| AttachedTo | gtirb::Module |
| Note     | Map from (typed) data objects to the encoding of the data,  expressed as a std::string containing an assembler encoding specifier: "string", "uleb128" or "sleb128". |


### elfSectionProperties

| <!-- --> | <!-- -->                                       |
|----------|------------------------------------------------|
| Label    | ```"elfSectionProperties"```                                |
| Type     | ```std::map<gtirb::UUID, std::tuple<uint64_t, uint64_t>>>>```        |
| Key      | The gtirb::UUID of a section. |
| Value    | The tuple with the ELF section types and flag.           |
| AttachedTo | gtirb::Module |
| Note     | Map from section UUIDs to tuples with the ELF section types and flags. |


### elfSymbolVersions

| <!-- --> | <!-- -->                                       |
|----------|------------------------------------------------|
| Label    | ```"elfSymbolVersions"```                                |
| Type     | ```std::tuple<std::map<uint16_t, std::tuple<std::vector<std::string>, uint16_t>>, std::map<std::string, std::map<uint16_t, std::string>>, std::map<gtirb::UUID, std::tuple<uint16_t, bool>>>```        |
| Key      | The gtirb::UUID of a section. |
| Value    | The tuple with the ELF section types and flag.           |
| AttachedTo | gtirb::Module |
| Note     | Tuple with symbol version definitions, needed symbol versions, and a mapping of symbol UUIDs to symbol versions. Symbol version definitions are  `ElfSymDefs = std::map<SymbolVersionId, std::tuple<std::vector<std::string>>, uint16_t>`, a map from symbol version identifiers version definitions. These correspond to `ELFxx_Verdef` entries in the ELF section `.gnu.version_d`. The values in the map are tuples containing the list of versions strings and the verdef flags. The verdef flag may be `VER_FLG_BASE` (0x1), which indicates that the given version definiton is the file itself, and must not be used for matching a symbol. The first element of the list is the version itself, the subsequent elements are predecessor versions. The needed symbol versions are `ElfSymVerNeeded = std::map<std::string, std::map<SymbolVersionId, std::string>>`, a map from dynamic library names to the symbol versions that they need. For each library, we have a map from version identifiers to version strings. Finally, symbol UUIDs are mapped to symbol versions as `ElfSymbolVersionsEntries = std::map<gtirb::UUID, std::tuple<SymbolVersionId,bool>>`, where the `bool` represents the `HIDDEN` attribute. Symbol version identifiers are `SymbolVersionId = uint16_t` integers. |


### cfiDirectives

| <!-- --> | <!-- -->                                       |
|----------|------------------------------------------------|
| Label    | ```"cfiDirectives"```                                |
| Type     | ```std::map<gtirb::Offset, std::vector<std::tuple<std::string, std::vector<int64_t>, gtirb::UUID>>>```        |
| Key      | The gtirb::Offset of a cfi directive. |
| Value    | cfi directive contains: a string describing the directive, a vector  of numeric arguments, and an optional symbolic argument (represented with the UUID of the symbol           |
| AttachedTo | gtirb::Module |
| Note     | Map from Offsets to  vector of cfi directives. A cfi directive contains: a string describing the directive, a vector  of numeric arguments, and an optional symbolic argument (represented with the UUID of the symbol). |


### elfSymbolInfo

| <!-- --> | <!-- -->                                       |
|----------|------------------------------------------------|
| Label    | ```"elfSymbolInfo"```                                |
| Type     | ```std::map<gtirb::UUID, std::tuple<uint64_t, std::string, std::string, std::string, uint64_t>>```        |
| Key      | The gtirb::UUID of a symbol. |
| Value    | The type, binding, and visibility categories of the symbol.           |
| AttachedTo | gtirb::Module |
| Note     | On ELF targets only: Map from symbols to their type, binding, and visibility categories. |


### libraries

| <!-- --> | <!-- -->                                       |
|----------|------------------------------------------------|
| Label    | ```"libraries"```                                |
| Type     | ```std::vector<std::string>```        |
| Value    | The name of a library.           |
| AttachedTo | gtirb::Module |
| Note     | Names of the external libraries that are needed dynamically at run time. |


### libraryPaths

| <!-- --> | <!-- -->                                       |
|----------|------------------------------------------------|
| Label    | ```"libraryPaths"```                                |
| Type     | ```std::vector<std::string>```        |
| Value    | A path contained in the rpath of the binary.           |
| AttachedTo | gtirb::Module |
| Note     | Paths contained in the rpath of the binary. |


### binaryType

| <!-- --> | <!-- -->                                       |
|----------|------------------------------------------------|
| Label    | ```"binaryType"```                                |
| Type     | ```std::vector<std::string>```        |
| Value    | A binary type descriptor.           |
| AttachedTo | gtirb::Module |
| Note     | A set of binary type descriptors e.g. for ELF whether the binary is PIE "DYN" or not, "EXEC". PE binaries have additional descriptors, "DLL" or "EXE, and subsystem descriptor, e.g. WINDOWS_GUI or WINDOWS_CUI. |


### SCCs

| <!-- --> | <!-- -->                                       |
|----------|------------------------------------------------|
| Label    | ```"SCCs"```                                |
| Type     | ```std::map<gtirb::UUID, int64_t>```        |
| Key      | The gtirb::UUID of a block |
| Value    | The intra-procedural SCC identifier of the block.           |
| AttachedTo | gtirb::Module |
| Note     | The intra-procedural SCC identifier of each block. |


### symbolicExpressionSizes

| <!-- --> | <!-- -->                                       |
|----------|------------------------------------------------|
| Label    | ```"symbolicExpressionSizes"```                                |
| Type     | ```std::map<gtirb::Offset, uint64_t>```        |
| Key      | The gtirb::Offset of a symbolic expression. |
| Value    | The size of the expression, in bytes.           |
| AttachedTo | gtirb::Module |
| Note     | Map from an Offset of a symbolic expression in a ByteInterval to its extent, a size in bytes. |


### peImportEntries

| <!-- --> | <!-- -->                                       |
|----------|------------------------------------------------|
| Label    | ```"peImportEntries"```                                |
| Type     | ```std::vector<std::tuple<uint64_t, int64_t, std::string, std::string>>```        |
| Value    | A tuples containing details of an imported function.          |
| AttachedTo | gtirb::Module |
| Note     | List of tuples detailing an imported function address, ordinal, function name, and library names for PE. |


### peExportEntries

| <!-- --> | <!-- -->                                       |
|----------|------------------------------------------------|
| Label    | ```"peExportEntries"```                                |
| Type     | ```std::vector<std::tuple<uint64_t, int64_t, std::string>>```        |
| Value    | A tuples containing details of an exported function.          |
| AttachedTo | gtirb::Module |
| Note     | List of tuples detailing an exported address, ordinal, and name for PE. |


### peImportedSymbols

| <!-- --> | <!-- -->                                       |
|----------|------------------------------------------------|
| Label    | ```"peImportedSymbols"```                                |
| Type     | ```std::vector<gtirb::UUID>```        |
| Value    | gtirb::UUID of an imported symbol.           |
| AttachedTo | gtirb::Module |
| Note     | UUIDs of the imported symbols for PE. |


### peExportedSymbols

| <!-- --> | <!-- -->                                       |
|----------|------------------------------------------------|
| Label    | ```"peExportedSymbols"```                                |
| Type     | ```std::vector<gtirb::UUID>```        |
| Value    | gtirb::UUID of an exported symbol.           |
| AttachedTo | gtirb::Module |
| Note     | UUIDs of the exported symbols for PE. |


### peResource

| <!-- --> | <!-- -->                                       |
|----------|------------------------------------------------|
| Label    | ```"peResource"```                                |
| Type     | ```std::vector<std::tuple<std::vector<uint8_t>, gtirb::Offset, uint64_t>>```        |
| Value    | A resource header, data length, and data pointer.           |
| AttachedTo | gtirb::Module |
| Note     | List of PE resources. A resource header, data length, and data pointer. |

### profile

| <!-- --> | <!-- -->                                                 |
|----------|----------------------------------------------------------|
| Label    | ```"profile"```                                          |
| Type     | ```std::map<gtirb:UUID,uint64_t>```                      |
| Key      | The gtirb::UUID of a gtirb::CodeBlock.                   |
| Value    | The number of times that block was executed.             |
| AttachedTo | gtirb::Module                                          |
| Notes    | An entry in this table describes how many times a code block was executed. Blocks that are not present in this aux data table should be assumed to have a value of 0, indicating that they were not executed. |

### functionNameProbabilities

| <!-- -->   | <!-- -->                                                                                                                                                                                                                                |
|------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Label      | ```"functionNameProbabilities"```                                                                                                                                                                                                       |
| Type       | ```std::map<std::string, std::map<gtirb::UUID, std::vector<std::tuple<std::string, std::string, float>>>>```                                                                                                                            |
| Key        | Name of the tool that made the predictions.                                                                                                                                                                                             |
| Value      | Map from function UUID to a list of weighted predictions.  Each prediction is a tuple of <function name, binary name, score> where the score's meaning is tool dependent. Several existing tools use the convention that higher score is a better match and values ranging from 0.0 to 1.0.                                                      |
| AttachedTo | gtirb::Module                                                                                                                                                                                                                           |
| Notes      | Used to collect results from tools that identify functions and their source libraries.  Source library information is tracked in [includedLibraryNames](#includedlibrarynames) and [includedlibraryVersions](#includedlibraryversions). |


### includedLibraryNames

| <!-- -->   | <!-- -->                                 |
|------------|------------------------------------------|
| Label      | ```"includedLibraryNames"```             |
| Type       | ```std::map<gtirb::UUID, std::string>``` |
| Key        | Included library UUID.                   |
| Value      | The name of the library.                 |
| AttachedTo | gtirb::Module                            |
| Notes      | Names of libraries that are included in an executable (i.e., their code is intermingled with the executable code). |


### includedlibraryVersions

| <!-- -->   | <!-- -->                                                                                                              |
|------------|-----------------------------------------------------------------------------------------------------------------------|
| Label      | ```"includedLibraryVersions"```                                                                                       |
| Type       | ```std::map<gtirb::UUID, std::string>```                                                                              |
| Key        | Included library UUID.                                                                                                |
| Value      | Version string for the included library.                                                                              |
| AttachedTo | gtirb::Module                                                                                                         |
| Notes      | Versions of libraries that are included in an executable (i.e., their code is intermingled with the executable code). |

### typeTable

| <!-- -->   | <!-- -->                                                                                                                                                                                                                                                                                                                    |
|------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Label      | ```"typeTable"```                                                                                                                                                                                                                                                                                                          |
| Type       | ```std::map<gtirb::UUID, std::variant<uint64_t, std::tuple<uint8_t>, std::tuple<int8_t, uint64_t>, uint64_t, uint64_t, std::tuple<gtirb::UUID, std::vector<gtirb::UUID>>, gtirb::UUID, std::tuple<gtirb::UUID, uint64_t>, tuple<uint64_t, std::vector<tuple<uint64_t, gtirb::UUID>>>, std::tuple<uint8_t>, gtirb::UUID>>``` |
| Key        | UUID of the type object                                                                                                                                                                                                                                                                                                     |
| Value      | Variant of the object type, where the variants field each represent: Unknown, Bool, Int, Char, Float, Function, Pointer, Array, Struct, Void, Alias, in that order.                                                                                                                                                         |
| AttachedTo | gtirb::Module                                                                                                                                                                                                                                                                                                               |
| Notes      | Contains structured type information about objects in the variant. Some empty tuples have been replaced with ```std::tuple<uint8_t>``` which is default-initialized to zero since some GTIRB implementations cannot store a 0-length tuple. The corresponding semantics of each type's variant field is the following:<br>- **Unknown**: ```uint64_t``` - Size of the unknown type<br>- **Bool**: ```std::tuple<uint8_t>``` - default initialized to zero<br>- **Int**: ```std::tuple<int8_t, uint64_t>``` - A tuple of signedness (1 for signed, 0 for unsigned) and width of int<br>- **Char**: ```uint64_t``` - Size of the character<br>- **Float**: ```uint64_t``` - size of the floating point number<br>- **Function**: ```std::tuple<gtirb::UUID, std::vector<gtirb::UUID>>``` - A tuple of return type UUID, and a list of parameter type UUIDs<br>- **Pointer**: ```gtirb::UUID``` - UUID of pointed-to type<br>- **Array**: ```std::tuple<gtirb::UUID, uint64_t>``` - A tuple of UUID of the elements of the array, and the number of element sin that array<br>- **Alias**: ```gtirb::UUID``` - The type being aliased (note this is effectively a `typedef`)<br>- **Struct**: ```tuple<uint64_t, std::vector<std::tuple<uint64_t, gtirb::UUID>>>``` - A tuple of the size of the structure in total, and a vector of its fields. Each field is represented as a tuple of the offset at which the field is located, and the UUID of the type of that field.<br>- **Void**: ```std::tuple<uint8_t>``` - default initialized to zero |

### prototypeTable


| <!-- -->   | <!-- -->                                   |
|------------|--------------------------------------------|
| Label      | ```"prototypeTable"```                     |
| Type       | ```std::map<gtirb::UUID, gtirb::UUID>```   |
| Key        | UUID of the function                       |
| Value      | UUID of the function type in the typeTable |
| AttachedTo | gtirb::Module                              |
| Notes      | Maps functions' UUIDs to their associated typeTable entry for the purpose of giving them prototypes. NOTE: The associated type table entry **must** be a Function type object. |
