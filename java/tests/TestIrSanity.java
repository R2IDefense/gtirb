package tests;

import static org.junit.jupiter.api.Assertions.*;

import com.grammatech.gtirb.*;
import com.grammatech.gtirb.Edge.EdgeType;
import com.grammatech.gtirb.Module;
import com.grammatech.gtirb.Module.FileFormat;
import com.grammatech.gtirb.Module.ISA;
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import org.junit.jupiter.api.Test;

public class TestIrSanity {

    @Test
    void testCreateSaveAndLoad() throws Exception {
        // Just create a simple IR w/ 1 module
        IR ir_orig = new IR();
        Module mod =
            new Module("c:/foo.exe", 0xCAFE, 0xBEEF, Module.FileFormat.PE,
                       Module.ISA.X64, "foo.exe");
        ir_orig.addModule(mod);
        mod.setByteOrder(Module.ByteOrder.LittleEndian);
        ir_orig.setCfg(new CFG(new ArrayList<Edge>(), new ArrayList<byte[]>()));

        File file;
        file = File.createTempFile("temp", null);

        String filename = file.getName();
        try {
            ir_orig.saveFile(filename);
        } catch (Exception e) {
            file.delete();
            throw e;
        }

        IR ir_reloaded;
        try {
            ir_reloaded = IR.loadFile(filename);
        } catch (Exception e) {
            file.delete();
            throw e;
        }

        file.delete();

        assertNotNull(ir_reloaded);
        Module mod_reloaded = ir_reloaded.getModules().get(0);
        assertEquals("foo.exe", mod_reloaded.getName());
        assertEquals(ir_reloaded.getVersion(), Version.gtirbProtobufVersion);
    }

    @Test
    void testIrSetAndGet() throws Exception {
        IR ir = new IR();

        // test addModules (list)
        List<Module> modules = new ArrayList<Module>();
        modules.add(new Module(
            "/opt/testModules/testModules-1.0.0/testModule0/bin/mod",
            0x8FFFFFFF00000201L, 0x0L, FileFormat.ELF, ISA.X64, "mod0"));
        modules.add(new Module(
            "/opt/testModules/testModules-1.0.0/testModule1/bin/mod",
            0x8FFFFFFF00000401L, 0x0L, FileFormat.ELF, ISA.X64, "mod1"));
        ir.addModules(modules);
        assertTrue(ir.getModules().equals(modules));

        // test get/set version
        ir.setVersion(1234);
        assertEquals(ir.getVersion(), 1234);

        // test set/get CFG
        List<Edge> edges = new ArrayList<Edge>();
        edges.add(new Edge(UUID.randomUUID(), UUID.randomUUID(),
                           EdgeType.Branch, false, false));
        edges.add(new Edge(UUID.randomUUID(), UUID.randomUUID(), EdgeType.Call,
                           true, false));
        List<byte[]> vertices = new ArrayList<byte[]>();
        vertices.add("OneSingleVertice".getBytes());
        CFG cfg = new CFG(edges, vertices);
        ir.setCfg(cfg);
        assertEquals(ir.getCfg(), cfg);
    }

    // TODO: The next few tests here each test different ways the loadFile()
    // function can fail. Unfortunately, in its current form, it only ever
    // returns null in each case, so we can't really tell that we're getting
    // the failure we expect. This should be improved if we ever make the
    // error reporting richer in the Java API.

    @Test
    void testNonGtirbContents() throws Exception {
        // A file with non-GTIRB contents.
        byte contents[] = "JUNK".getBytes(Charset.forName("ASCII"));
        ByteArrayInputStream file_proxy = new ByteArrayInputStream(contents);
        IR ir;
        ir = IR.loadFile(file_proxy);

        // IR should be null here
        assertNull(ir);
    }

    @Test
    void testWrongVersion() throws Exception {
        // A GTIRB file w/ the wrong version.
        ByteArrayOutputStream content_builder = new ByteArrayOutputStream();
        content_builder.write("GTIRB".getBytes(Charset.forName("ASCII")));
        content_builder.write(0);
        content_builder.write(0);
        content_builder.write(255);

        byte contents[] = content_builder.toByteArray();
        ByteArrayInputStream file_proxy = new ByteArrayInputStream(contents);
        IR ir;
        ir = IR.loadFile(file_proxy);

        // IR should be null here
        assertNull(ir);
    }

    @Test
    void testCorruptedProtobuf() throws Exception {
        // A GTIRB file w/ the right version but bad protobuf.
        ByteArrayOutputStream content_builder = new ByteArrayOutputStream();
        content_builder.write("GTIRB".getBytes(Charset.forName("ASCII")));
        content_builder.write(0);
        content_builder.write(0);
        content_builder.write(Version.gtirbProtobufVersion);
        content_builder.write(255);

        byte contents[] = content_builder.toByteArray();
        ByteArrayInputStream file_proxy = new ByteArrayInputStream(contents);
        IR ir;
        ir = IR.loadFile(file_proxy);

        // IR should be null here
        assertNull(ir);
    }

    @Test
    void testAddAndRemoveModules() throws Exception {
        IR ir = new IR();
        Module mod0 = new Module("/usr/bin/mod0", 0x0000, 0x0FFF,
                                 FileFormat.ELF, ISA.X64, "mod0");
        ir.addModule(mod0);
        Module mod1 = new Module("/usr/bin/mod1", 0x1000, 0x1FFF,
                                 FileFormat.ELF, ISA.X64, "mod1");
        ir.addModule(mod1);
        Module mod2 = new Module("/usr/bin/mod2", 0x2000, 0x2FFF,
                                 FileFormat.ELF, ISA.X64, "mod2");
        ir.addModule(mod2);
        List<Module> modules = ir.getModules();
        assertEquals(modules.size(), 3);

        ir.removeModule(mod0);
        ir.removeModule(mod2);

        // Now the only module left should be "mod1"
        assertEquals("mod1", ir.getModules().get(0).getName());
        assertEquals(mod1.getIr().get(), ir);
        assertTrue(mod0.getIr().isEmpty());
        assertTrue(mod2.getIr().isEmpty());
    }

    @Test
    void testIrFindModules() throws Exception {
        IR ir = new IR();

        // test addModules (list)
        List<Module> modules = new ArrayList<Module>();
        modules.add(new Module(
            "/opt/testModules/testModules-1.0.0/testModule0/bin/mod",
            0x8FFFFFFF00000201L, 0x0L, FileFormat.ELF, ISA.X64, "mod0"));
        modules.add(new Module(
            "/opt/testModules/testModules-1.0.0/testModule1/bin/mod",
            0x8FFFFFFF00000401L, 0x0L, FileFormat.ELF, ISA.X64, "mod1"));
        modules.add(new Module(
            "/opt/testModules/testModules-1.0.0/testModule1/bin/mod-dup",
            0x8FFFFFFF00000401L, 0x0L, FileFormat.ELF, ISA.X64, "mod1"));
        ir.addModules(modules);
        assertTrue(ir.getModules().equals(modules));

        List<Module> mod0_modules = ir.findModules("mod0");
        assertEquals(1, mod0_modules.size());
        for (Module module : mod0_modules) {
            assertEquals("mod0", module.getName());
        }

        List<Module> mod1_modules = ir.findModules("mod1");
        assertEquals(2, mod1_modules.size());
        for (Module module : mod1_modules) {
            assertEquals("mod1", module.getName());
        }
    }
}
