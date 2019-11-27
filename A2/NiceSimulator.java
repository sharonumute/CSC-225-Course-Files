/* NiceSimulator.java
   CSC 225 - Summer 2017

   An empty shell of the operations needed by the NiceSimulator
   data structure.

   B. Bird - 06/11/2017

   Student Name: Sharon Umute
   StudentID: V00852291
*/


import java.io.*;
import java.util.*;
import java.lang.Object;
import java.util.regex.*;

public class NiceSimulator{

	public static final int SIMULATE_IDLE = -2;
	public static final int SIMULATE_NONE_FINISHED = -1;
	private Task[] tasks;
    private Task[] tree;
    private Task root;
    private int size;

	/* Constructor(maxTasks)
	   Instantiate the data structure with the provided maximum
	   number of tasks. No more than maxTasks different tasks will
	   be simultaneously added to the simulator, and additionally
	   you may assume that all task IDs will be in the range
	     0, 1, ..., maxTasks - 1
	*/
	public NiceSimulator(int maxTasks){
		tasks=new Task[maxTasks];
        tree=new Task[maxTasks];
        size=0;
	}

	/* taskValid(taskID)
	   Given a task ID, return true if the ID is currently
	   in use by a valid task (i.e. a task with at least 1
	   unit of time remaining) and false otherwise.

	   Note that you should include logic to check whether
	   the ID is outside the valid range 0, 1, ..., maxTasks - 1
	   of task indices.

	*/
	public boolean taskValid(int taskID){
		if (taskID>=tasks.length || tasks[taskID]==null){
			return false;
		}
		else if (tasks[taskID].time >= 1){
			return true;
		}else{
			return false;
		}
	}

	/* getPriority(taskID)
	   Return the current priority value for the provided
	   task ID. You may assume that the task ID provided
	   is valid.

	*/
	public int getPriority(int taskID){
		return tasks[taskID].priority;
	}

	/* getRemaining(taskID)
	   Given a task ID, return the number of timesteps
	   remaining before the task completes. You may assume
	   that the task ID provided is valid.

	*/
	public int getRemaining(int taskID){
		return tasks[taskID].time;
	}


	/* add(taskID, time_required)
	   Add a task with the provided task ID and time requirement
	   to the system. You may assume that the provided task ID is in
	   the correct range and is not a currently-active task.
	   The new task will be assigned nice level 0.
	*/
	public void add(int taskID, int time_required){
		if(time_required>0){
            Task temp=new Task(taskID,0, time_required);
			temp.index=size;
            tasks[taskID]=temp;
            tree[size]=temp;
            if(size>0){
                bubbleup(size);
            }
            size++;
		}
	}

   

	/* kill(taskID)
	   Delete the task with the provided task ID from the system.
	   You may assume that the provided task ID is in the correct
	   range and is a currently-active task.
	*/
	public void kill(int taskID){
        Task temp =tree[tasks[taskID].index];
        tree[tasks[taskID].index]=tree[size-1];
        tasks[tree[tasks[taskID].index].id].index=tasks[taskID].index;
		tree[size-1]=null;
		if(size>2){
			bubbledown(tasks[taskID].index);
		}
        tasks[taskID]=null;
        size--;
	}


	/* renice(taskID, new_priority)
	   Change the priority of the the provided task ID to the new priority
       value provided. The change must take effect at the next simulate() step.
	   You may assume that the provided task ID is in the correct
	   range and is a currently-active task.

	*/
	public void renice(int taskID, int new_priority){
        if(tasks[taskID]!=null){
            int temp =tasks[taskID].priority;
            tasks[taskID].priority=new_priority;
            tree[tasks[taskID].index].priority=new_priority;
            if (new_priority>temp){
                bubbledown(tasks[taskID].index);
            }else if(new_priority<temp){
                bubbleup(tasks[taskID].index);
            }
        }
	}

    private void bubbleup(int i){
        while(i!=0){
            if(tree[i].isLessThan(tree[(i-1)/2])){
                Task temp =tree[(i-1)/2];
                tree[(i-1)/2]=tree[i];
                tree[i]=temp;
                tasks[tree[i].id].index=i;
                tasks[tree[(i-1)/2].id].index=(i-1)/2;
                i=(i-1)/2;
            }else{
				break;
			}
        }
        
    }

    private void bubbledown(int i){
        while(i>=0 && i<size-1){
			if((tree[2*i+1]!=null && !tree[i].isLessThan(tree[2*i+1]))||(tree[2*i+2]!=null && !tree[i].isLessThan(tree[2*i+2]))){
				if(tree[2*i+1]!=null){
					Task temp =tree[2*i+1];
					tree[2*i+1]=tree[i];
					tree[i]=temp;
					tasks[tree[i].id].index=i;
					tasks[tree[2*i+1].id].index=2*i+1;
					i=2*i+1;
				}else if(tree[2*i+2]!=null){
					Task temp =tree[2*i+2];
					tree[2*i+2]=tree[i];
					tree[i]=temp;
					tasks[tree[i].id].index=i;
					tasks[tree[2*i+2].id].index=2*i+2;
					i=2*i+2;
				}
			}else{
				break;
			}
		}
    }

	/* simulate()
	   Run one step of the simulation:
		 - If no tasks are left in the system, the CPU is idle, so return
		   the value SIMULATE_IDLE.
		 - Identify the next task to run based on the criteria given in the
		   specification (tasks with the lowest priority value are ranked first,
		   and if multiple tasks have the lowest priority value, choose the
		   task with the lowest task ID).
		 - Subtract one from the chosen task's time requirement (since it is
		   being run for one step). If the task now requires 0 units of time,
		   it has finished, so remove it from the system and return its task ID.
		 - If the task did not finish, return SIMULATE_NONE_FINISHED.
	*/
	public int simulate(){
		if (tree[0]!=null){
			int tempID=tree[0].id;
			int currTime=tree[0].time;
			currTime--;
			tasks[tempID].time=currTime;
            tree[0].time=currTime;

			if (currTime==0){
				this.kill(tempID);
				return tempID;
			}else{
				return SIMULATE_NONE_FINISHED;
			}
		}else{
			return SIMULATE_IDLE;
		}
	}
}


class Task{
	public int priority;
	public int time;
    public int id;
    public int index;

	public Task(int id, int priority, int time){
        this.id=id;
		this.priority=priority;
		this.time=time;
	}

    public boolean isLessThan(Task b){
        if(this.priority<=b.priority){
            if(this.priority==b.priority){
                if(this.id<b.id){
                    return true;
                }else{
                    return false;
                }
            }else{
                return true;
            }

        }else{
            return false;
        }
    }
}
