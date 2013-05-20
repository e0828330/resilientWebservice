package monitor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MonitorManager {
	
	private Map<String, Thread> monitors = new ConcurrentHashMap<String, Thread>();
	
	private static MonitorManager instance;
	
	private MonitorManager() {
		
	}
	
	/**
	 * Returns the monitorManager instance
	 * 
	 * @return
	 */
	public static MonitorManager getInstance() {
		if (instance != null) {
			instance = new MonitorManager();
		}
		return instance;
	}

	/**
	 * Adds a new monitor to the manager and starts it.
	 * Any already running instance will be replaced.
	 * 
	 * @param monitor
	 */
	public synchronized void addMonitor(Monitor monitor) {
		// Stop running monitor if any
		stopMonitor(monitor.getService());
		
		// Start new one
		Thread monitorThread = new Thread(monitor);
		monitors.put(monitor.getService(), monitorThread);
		monitorThread.start();
	}

	/**
	 * Stops a monitor instance for a given service
	 * 
	 * @param service
	 */
	public synchronized void stopMonitor(String service) {
		if (monitors.containsKey(service)) {
			monitors.get(service).interrupt();
			monitors.remove(service);
		}
	}

	/**
	 * Returns whether the service already has a running monitor
	 * 
	 * @param service
	 * @return
	 */
	public synchronized boolean isMonitored(String service) {
		return monitors.containsKey(service);
	}
	
}
