package com.be3short.fxtask;
// User Access: true

import java.util.ArrayList;
import java.util.Scanner;

import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.stage.Stage;

public class TaskManager extends Application
{

	private static ArrayList<TaskDescriptor> tasks = new ArrayList<TaskDescriptor>();
	private static String name;
	private static TaskManager manager; // static access to task manager instance
	public static Scanner in = new Scanner(System.in);
	public static boolean done = false;
	static Stage stage;

	public static void closeStage()
	{
		stage.show();
		stage.close();
	}

	/*
	 * Creates a new stage from the main application thread
	 */
	public Stage getNewStage()
	{
		Stage st = new Stage();
		return st;
	}

	/*
	 * Starts the task manager application
	 * 
	 * (non-Javadoc)
	 * 
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage primaryStage) throws Exception
	{

		manager = this;
		stage = primaryStage;
		if (tasks.size() <= 1)
		{
			try
			{
				runTask(tasks.get(0));
			} catch (Exception e)
			{
				// TODO: handle exception
			}
		} else
		{
			boolean done = false;
		}
		runMenuTasks();

	}

	//runMenuTasks();
	public static void newTask()
	{
		Task<Integer> task = new Task<Integer>()
		{

			@Override
			protected Integer call() throws Exception
			{
				final BooleanProperty success = new SimpleBooleanProperty(false);
				Long attemptTime = System.currentTimeMillis() + (long) 20;
				while (!success.getValue())
				{
					if (attemptTime <= System.currentTimeMillis())
					{
						try
						{

							//in = new Scanner(System.in);
							//new TaskManager().runMenuTasks();

							success.setValue(true);

						} catch (Exception e)
						{
							e.printStackTrace();
							// Console.error("Unable to create output plot: " + output.toString());
						}
						// your code here

						// }
						// }, (int) 500, (int) 500);
						attemptTime = System.currentTimeMillis() + 5000;
					}
				}
				return 1;
			}

			@Override
			protected void succeeded()
			{
				super.succeeded();
				runMenuTasks();
				if (!done)
				{
					newTask();
				}
			}

		};
		new Thread(task).start();

	}

	//			Thread debugStatusThread = new Thread(new Runnable()
	//			{
	//
	//				public void run()
	//				{
	//					while (!done)
	//					{
	//						in = new Scanner(System.in);
	//						String inn = in.nextLine();
	//						System.out.println(inn);
	//						if (inn.equals("true"))
	//						{
	//							runMenuTasks();
	//						}
	//
	//					}
	//				}
	//			});
	//			debugStatusThread.start();

	public static Stage createStage()
	{
		if (manager != null)
		{
			return manager.getNewStage();
		} else
		{
			return null;
		}
	}

	public static void runTask(TaskDescriptor item)
	{
		item.actions();
	}

	public static void loadTasks(TaskDescriptor... queue)
	{
		for (TaskDescriptor task : queue)
		{
			tasks.add(task);
		}
	}

	public static void launchMenu(TaskDescriptor... queue)
	{
		launchMenu("Java FX Task Manager", queue);
	}

	public static void launchMenu(String title, TaskDescriptor... queue)
	{
		name = title;
		loadTasks(queue);
		launch();

	}

	private static void runMenuTasks()
	{
		Task<Integer> task = new Task<Integer>()
		{

			@Override
			protected Integer call() throws Exception
			{
				Integer index = -1;
				String input = "";
				printMenu();
				try
				{
					//	manager.in = new Scanner(System.in);
					if (manager.in.hasNextLine())
					{
						input = manager.in.nextLine();

						if (input.equals("q"))
						{
							System.exit(0);
						}
						// in.close();
						System.out.println("input " + input);
						index = Integer.parseInt(input);
						if (tasks.size() > index && index >= 0)
						{
							try
							{

								//	newTask();
							} catch (Exception e)
							{
								e.printStackTrace();
							}
						}
					} else
					{
						return -1;
					}
				} catch (

				Exception badInput)
				{
					System.err.println("Bad input " + input + ", please try again\n");

				}
				return index;
			}

			@Override
			protected void succeeded()
			{
				super.succeeded();
				if (super.getValue() <= -2)
				{
					done = true;
					closeStage();
				} else
				{
					try
					{
						runTask(tasks.get(super.getValue()));
					} catch (

					Exception badInput)
					{

					}
					runMenuTasks();
				}

				//				if (!done)
				//				{
				//					runMenuTasks();
				//				}
			}

		};
		new Thread(task).start();

	}

	private static void printMenu()
	{
		int ind = 0;
		System.out.println(name + " :");
		for (TaskDescriptor task : tasks)
		{
			System.out.println(ind + " : " + task.title());
			ind++;
		}
		System.out.println("q : quit");
		System.out.println("Enter task index to execute >> ");
	}

}
