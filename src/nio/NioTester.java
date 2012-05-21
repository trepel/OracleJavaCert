package nio;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.DosFileAttributeView;
import java.nio.file.attribute.DosFileAttributes;
import java.util.Map;
import java.util.logging.SimpleFormatter;

public class NioTester {
    
    public void pathTest() {
        
        Path p1              = Paths.get("/home/trepel", "sources/OracleJavaCert", "pathTesting", "subdir1", "file1.txt"); //result: /home/trepel/OracleJavaCert/pathTesting/subdir1/file1.txt
        Path doubleSlashes  = Paths.get("//home//trepel"); // it is ok, the string is normalized
        Path isEqualsDS     = Paths.get("/home/./trepel/OracleJavaCert/pathTesting/../.."); // NOT
        Path p2             = doubleSlashes.resolve("OracleJavaCert"); // /home/trepel/OracleJavaCert
        Path relative       = Paths.get("trepel"); //getRoot and getParent returns null
        
        System.out.format("toString: %s%n", p1.toString());
        System.out.format("toString: %s%n", doubleSlashes.toString());
        System.out.format("toString: %s%n", isEqualsDS.toString()); // the . and .. are not normalized
        
        System.out.println(p1.getName(0)); // returns home (0 index doesn't point to root element
        System.out.println(p1.getNameCount()); // returns 6 (root element doesn't count, file in the end does)
        System.out.println("" + relative.getParent() + relative.getRoot()); // returns nullnull
        
        try {
            Path fp = p1.toRealPath();
        } catch (NoSuchFileException x) {
//            System.err.format("%s: no such" + " file or directory%n", p1);
            System.out.println("File is not found");
            System.out.println(Files.isReadable(p1));
            File f = p1.toAbsolutePath().toFile();
            System.out.println(f.exists());
        } catch (IOException x) {
            System.err.format("%s%n", x);
        }
        
        Path p1_to_p2 = p1.relativize(p2); // relative path from p1 to p2, the result contains .. very often
        System.out.println(doubleSlashes.equals(isEqualsDS)); // false
        
        
        System.out.println("-------------FileAttributes--------");
        try {
            System.out.println(Files.getFileAttributeView(p1, DosFileAttributeView.class).name());
            Map<String, Object> attrMap = Files.readAttributes(p1, "dos:*");
            for (String key : attrMap.keySet()) {
                System.out.println(key + ": " + attrMap.get(key));
            }
            DosFileAttributes dfa = Files.readAttributes(p1, DosFileAttributes.class);
            System.out.println(dfa.fileKey());
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        
        
        // ============================= Files class ========================================
        
        System.out.println();
        System.out.println("============================ FILES");
        System.out.println();
        
        try {
            System.out.println(Files.isSameFile(doubleSlashes, isEqualsDS));
            
            System.out.println(Files.getOwner(p2)); // NoSuchFileException if file doesn't exist
            
            DosFileAttributes attr = Files.readAttributes(p2, DosFileAttributes.class);
            System.out.println(attr.lastAccessTime());
            System.out.println(attr.isReadOnly());
            
            // example of usage SimpleFileVisitor<Path>
            // in visitFile method often the PathMatcher is used
            System.out.println();
            final PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:" + "**.pdf"); // ** spans across path.separator 
            Files.walkFileTree(p2, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {
                    if (attr.isSymbolicLink()) {
                        System.out.format("Symbolic link: %s ", file);
                    } else if (attr.isRegularFile()) {
                        if (matcher.matches(file)) { // matcher.matches(file.getFileName()) has to be used for pattern *.pdf to work ok
                            System.out.format("PDF file: %s ", file);
                        } else {
                            System.out.format("Regular file: %s ", file);
                        }
                    } else {
                        System.out.format("Other: %s ", file);
                    }
                    System.out.println("(" + attr.size() + "bytes)");
                    return FileVisitResult.CONTINUE;
                }
                
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    System.out.format("Directory: %s%n", dir);
                    return FileVisitResult.CONTINUE;
                }
            });
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        System.out.println();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(p2, "*.pdf")) {
            for (Path file: stream) {
                System.out.println(file.getFileName());
            }
        } catch (IOException | DirectoryIteratorException x) {
            // IOException can never be thrown by the iteration.
            // In this snippet, it can only be thrown by newDirectoryStream.
            System.err.println(x);
        }
        
        
        
        
    }

}
