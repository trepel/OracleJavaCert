package nio;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WatcherHandler implements Runnable {
    
    WatchService watcher;
    Map<WatchKey, Path> keys;
    
    public WatcherHandler() throws IOException {
            watcher = FileSystems.getDefault().newWatchService();
            keys = new HashMap<WatchKey, Path>();
    }
    
    public void register(Path dir) throws IOException {
        if (!Files.isDirectory(dir)) {
            return;
        }
        WatchKey key = dir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
        keys.put(key, dir);
    }
    
    public void processEvents() {
        
        for(;;) {
            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
            
            // get the dir the event is related to (inside this directory has something happend
            Path watchedDir = keys.get(key);
            if (watchedDir == null) {
                continue;
            }
            
            List<WatchEvent<?>> events = key.pollEvents();
            for (WatchEvent<?> event : events) {
                event = (WatchEvent<Path>) event;
                Path name = (Path) event.context();
                Path child = watchedDir.resolve(name); // this is how to get the absolute path for file inside the watched directory
                Kind kind = event.kind();
                
                if (kind == StandardWatchEventKinds.OVERFLOW) {
                    continue;
                }
                
                // common processing for all other event (create, modiry, delete)
                System.out.format("%s: %s\n", kind  .name(), child);
            }
            System.out.println("POLL EMPTY");
            
            boolean valid = key.reset(); // after processing I have to reset the key to be able to poll new events
            if (!valid) {
                keys.remove(key); // if key is invalid, corresponding watched directory is inaccesible and no event will ever come, doesn't need to be watched anymore
                
                if (keys.isEmpty()) { // if every watched directory becomes inaccessible, do not loop ifinitely to watch events, no event will ever come
                    break;
                }
            }
            
        }
    }

    @Override
    public void run() {
        this.processEvents();
    }

}
