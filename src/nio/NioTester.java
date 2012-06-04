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
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.DosFileAttributeView;
import java.nio.file.attribute.DosFileAttributes;
import java.util.Map;

public class NioTester {
    
    public void pathTest() {
        
        Path p1              = Paths.get("/home/trepel", "sources/OracleJavaCert", "pathTesting", "subdir1", "file1.txt"); //result: /home/trepel/OracleJavaCert/pathTesting/subdir1/file1.txt
        Path doubleSlashes  = Paths.get("//home//trepel"); // it is ok, the string is normalized
        Path isEqualsDS     = Paths.get("/home/./trepel/OracleJavaCert/pathTesting/../.."); // not normalized, probably due to symlinks
    	// you can use p1.normalize() method, but it doesn't check if symlinks presence makes some mess
        Path p2             = doubleSlashes.resolve("OracleJavaCert"); // /home/trepel/OracleJavaCert
        Path relative       = Paths.get("trepel"); //getRoot and getParent returns null
        
        System.out.format("toString: %s%n", p1.toString());
        System.out.format("toString: %s%n", doubleSlashes.toString());
        System.out.format("toString: %s%n", isEqualsDS.toString()); // the . and .. are not normalized
        
        System.out.println(p1.getName(0)); // returns home (0 index doesn't point to root element
        System.out.println(p1.getNameCount()); // returns 7 (root element doesn't count, file in the end does)
        System.out.println("" + relative.getParent() + relative.getRoot()); // returns nullnull
        
        System.out.println("" + p1.getParent()); // return full path to the parent (not only one-name path)
        
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
        //relativize always works for relative paths, if one is absolute, on relative, an exception is thrown
        // if both are absolute, the behaviour is system dependent (probably cannot create relative path from D:\ to C:\ on Windows for example)
//        Path p1_to_relative = p1.relativize(relative);
        
        System.out.println(doubleSlashes.equals(isEqualsDS)); // false, paths themselves are not the same
        try {
			System.out.println(doubleSlashes.toRealPath().equals(isEqualsDS.toRealPath())); // true, the paths pointed to the same "real" file
		} catch (IOException e2) {
			System.err.println("Path " + doubleSlashes + " doesn't exist");
			e2.printStackTrace();
		}
        
        System.out.println("Iteration over path");
        for (Path subpath : p1) {
			System.out.print(subpath.toString() + ":" + subpath.isAbsolute() + " ");
		}
        System.out.println();
        
        System.out.println("-------------FileAttributes--------");
        try {
            System.out.println(Files.getFileAttributeView(p1, DosFileAttributeView.class).name());
            Map<String, Object> attrMap = Files.readAttributes(p1, "dos:*");
            for (String key : attrMap.keySet()) {
                System.out.println(key + ": " + attrMap.get(key));
            }
            System.out.println("");
            DosFileAttributes dfa = Files.readAttributes(p1, DosFileAttributes.class);
            System.out.println(dfa.fileKey());
            System.out.println(dfa.creationTime());
            
            System.out.println();
            System.out.println("Get attribute method:");
            System.out.println(Files.getAttribute(p1, "dos:hidden")); // dos:* throws IllegalArgumentException
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        
        
        
        // ============================= Files class ========================================
        
        System.out.println();
        System.out.println("============================ FILES");
        System.out.println();
        
        try {
            System.out.println(Files.isSameFile(doubleSlashes, isEqualsDS)); // true cos "real" file the paths pointed to exists and is the same for both
            
            System.out.println(Files.getOwner(p2)); // NoSuchFileException if file doesn't exist
            
            DosFileAttributes attr = Files.readAttributes(p2, DosFileAttributes.class);
            System.out.println(attr.lastAccessTime());
            System.out.println(attr.isReadOnly());
            
            System.out.println("");
            
            Path nonEmptyDir = Paths.get("/home/trepel/sources/OracleJavaCert/pathTesting/nonEmptyDir");
            Path realFile = Paths.get("/home/trepel/sources/OracleJavaCert/pathTesting/nonEmptyDir/a.txt");
            Path emptyDir = Paths.get("/home/trepel/sources/OracleJavaCert/pathTesting/emptyDir");
            Path copiedDir = Paths.get("/home/trepel/sources/OracleJavaCert/pathTesting/copyOrMoveSomethingHere/copiedNonEmptyDir");
            Path targetFile = Paths.get("/home/trepel/sources/OracleJavaCert/pathTesting/copyOrMoveSomethingHere/copiedFileA.txt");
            Files.copy(nonEmptyDir, copiedDir); // this works, but the nonEmptyDir is copied without content
            Files.delete(copiedDir);
            
            // note the path to file has to be specified, not the path to desired target directory, where we want to copy the file
            Files.copy(realFile, targetFile); // works, but next call has to be with replace_existing, cos the file already exists
            Files.copy(realFile, targetFile, StandardCopyOption.REPLACE_EXISTING);
            Files.delete(targetFile);
            
            Files.move(nonEmptyDir, copiedDir); // on unix, the dir is moved with its content, cos moving dir == renaming dir (in i-node table)
            System.out.println("was copied the content too? " + 
            		Files.exists(Paths.get("/home/trepel/sources/OracleJavaCert/pathTesting/copyOrMoveSomethingHere/copiedNonEmptyDir/a.txt")));
            Files.move(copiedDir, nonEmptyDir);
            
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
    
    public static void main(String[] args) {
    	NioTester nioT = new NioTester();
        nioT.pathTest();
        
	}

}
