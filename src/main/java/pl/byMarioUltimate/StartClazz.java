package pl.byMarioUltimate;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

/**
 * 
 * @author Mariusz Lewandowski; byMario
 */
@Service
public class StartClazz{

	List<Thread> tl;
//	@PostConstruct
	public void onInit() {
//		tl = new ArrayList<Thread>();
//		 System.out.println("Hello from a thread! S " +  Thread.currentThread().getName());
		 Thread t;
//		for(int i = 0; i < 2; i++) {
			
//		(new Thread(this)).start();
			t= new Thread(new NeuralNetwork(2,2,1));
			t.setName("NET");
//			tl.add(t);
			
			
//		}
		
//		System.out.println(tl.size());
//		for(Thread th : tl) {
//			th.start();
//		}
		
		t.start();
		while (t.isAlive()){
			t.run();
			
		}
		

	}

	public class HelloRunnable implements Runnable {

	    public void run() {
	    	try {
				Thread.sleep(1000);
				System.out.println("Hello from a thread! " +  Thread.currentThread().getName());
			}
			catch(InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }


	}

}
