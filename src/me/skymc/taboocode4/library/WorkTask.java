package me.skymc.taboocode4.library;

import java.util.*;

import me.skymc.taboocode4.TabooCode4;

public class WorkTask {
	
	public final int nThreads;
	public static PoolWorker[] threads;
	
	public final LinkedList<Runnable> queue = new LinkedList<>();

	public WorkTask(int nThreads) {
		
		this.nThreads = nThreads;
		threads = new PoolWorker[nThreads];

		for (int i = 0; i < nThreads; i++) {
			threads[i] = new PoolWorker();
			threads[i].start();
		}
	}
	
	public void stop() {
		
		for (PoolWorker p : threads) {
			p.stop();
		}
	}

	public void execute(Runnable r) {
		
		synchronized (queue) {
			queue.addLast(r);
			queue.notify();
		}
	}

	private class PoolWorker extends Thread
	{
		public void run() 
		{
			Runnable r;
			
			while (true) {
				synchronized (queue) {
					while (queue.isEmpty()) {
						try {
							queue.wait();
						} 
						catch (InterruptedException ignored) {
						}
					}
					r = queue.removeFirst();
				}
				try  {
					r.run();
				} 
				catch (RuntimeException e) {
					if (TabooCode4.isDebug()) {
						TabooCode4.send("&c异步任务出现错误 详细信息:");
						for (int i = 0; i < e.getStackTrace().length && i < 10 ; i++) {
							String name = e.getStackTrace()[i].getClassName();
							
							try {
								TabooCode4.send("&f("+i+")位置: &8"+name.substring(0, name.lastIndexOf(".")));
								TabooCode4.send("&7     类名: &8"+e.getStackTrace()[i].getFileName().replaceAll(".java", ""));
								TabooCode4.send("&7     行数: &8"+e.getStackTrace()[i].getLineNumber());
							}
							catch (Exception e2) {
								
							}
						}
					}
				}
			}
		}
	}
}