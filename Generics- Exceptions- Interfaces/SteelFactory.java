import java.util.ArrayList;
/**
 * A steel factory (an implementation of an enterprise)
 * represented by its name and list of employees.
 * @author AlgoDat team
 *
 */
public class SteelFactory implements Enterprise {
  
    /** Name of the factory */
    private String name;
    /** List of workers */
    private ArrayList<Worker> workers;

    /**
     * Creates a new steel factory with a given name
     * @param name Name of the factory
     */
    public SteelFactory(String name) {
    	this.name=name;
    	workers = new ArrayList<Worker>();
    }

    @Override
    public void addWorker(String firstName, String lastName) {
    	this.workers.add(new Worker(firstName, lastName));
    }

    @Override
    public String getName() {
    	return this.name;
    }

    @Override
    public int compareTo(Enterprise o) {
    	return this.workers.size()-o.getWorkerCount();
    }

    @Override
    public int getWorkerCount() {
    	return this.workers.size();
    }
}

