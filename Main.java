import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//The pizza delivery service processes its orders on the first come, first served basis. 
//After a client has paid for his/her order, he/she is added to the online waiting list. 
//After the pizza is ready, the information is passed to a courier, saved to a file, and removed from the waiting list. 
//Display the number of customers who are currently waiting for an order.

class Pizza
{ //class for order data
	String name;
	float cost;
	int quantity;
	
	public Pizza(String name, float cost, int quantity){
		this.name = name;
		this.cost = cost;
		this.quantity = quantity;
	}
	
	@Override
	public String toString(){
		return "pizza: " + name + " " + cost + " " + quantity;
	}
	
}


class Order
{ //class for an order
	public String name;
	public String address;
	List <Pizza> pizzas;
	float sum = 0;
	
	public Order(String name, String address, List <Pizza> pizzas, float sum){
		this.name = name;
		this.address = address;
		this.pizzas = pizzas;
		this.sum = sum;
	}
	
	@Override
	public String toString(){
		return "Name is: " + this.name + "; Address is " + this.address + "; Total amount " + this.sum;
	}
}


class Item
{//class for item in queue
	private Order order;
	private Item next;
	
	public Order getOrder(){
		return order;
	}
	public void setOrder(Order order){
		this.order = order;
	}
	
	public Item getNext(){
		return next;
	}
	public void setNext(Item next){
		this.next = next;
	}
}


class MyQueue
{ //class for the order queue
	Item head = null;
	Item tail = null;
	private int size = 0;
	
	public void push(Order obj){
		Item tempItem = new Item();
		tempItem.setOrder(obj);
		if (head == null){
			head = tempItem;
		} else {
			tail.setNext(tempItem);
		}
		tail = tempItem;
		size++;
	}
	public Order pop(){
		if (size == 0){
			System.out.println("No orders");
			return null;
		}
		Order obj = head.getOrder();
		head = head.getNext();
		if (head == null) 
			tail = null;
		size--;
		System.out.println("The order deleted");
		return obj;
	}
	public Order get(int index){
		if (size == 0 || index >= size || index < 0){
			return null;
		}
		Item current = head;
		int pos = 0;
		while (pos < index){
			current = current.getNext();
			pos++;
		}
		Order obj = current.getOrder();
		return obj;
	}
	
	void saveFile(String fileName, Order order) throws IOException {
		File file = new File(fileName);
		FileWriter fileWrite = new FileWriter(file, true);
		fileWrite.write("___________________________\n");
		fileWrite.write(order.toString() + "\n");
		fileWrite.write("___________________________\n");
		fileWrite.close();
		
	}
	public int size(){
		return size;
	}
	
	public void show(){
		int pos = 1;
		Item current = head;
		if (head != null) {
			System.out.println("___________________________");
			System.out.println("| Order");
			System.out.println("___________________________");
			while (current != null){
				Order ord = current.getOrder();
				System.out.printf("%d| Name %15s| Address %15s| Total amount %.2f", pos, ord.name, ord.address, ord.sum);
				//System.out.println(pos + " " + ord.name + " " + ord.address + " " + ord.sum);
				System.out.println("\n___________________________");
				current = current.getNext();
				pos++;
			}
		} else System.out.println("No orders");
		
	}
	void getQuantity(){
		Item current = head;
		int count = 0;
		if (head == null)
			System.out.println("No orders");
		else {
			while (current != null) {
				count++;
				current = current.getNext();
			}
			System.out.println("Number of clients " + count);
		}
	}
	
}


public class Main
{
	private static int mainMenu(){
		System.out.print("\n1 - add order to the queue\n" +
						"2 - deleting an order from the queue\n" +
						"3 - count orders\n" +
						"4 - output queue\n" +
						"0 - exit the program\n");
		Scanner scan = new Scanner(System.in);
		int choice = scan.nextInt();
		return choice;
	}
	
	private static Order insert(){
		Scanner in = new Scanner(System.in);
		System.out.println("Enter the name: ");
		String name = in.nextLine();
		System.out.println("Enter the address: ");
		String address = in.nextLine();
		
		int finish = 1;
		float sum = 0;
		String pizzaName;
		float cost;
		int quantity;
		List <Pizza> pizzaList = new ArrayList<Pizza>();
		while(finish != 0){
			System.out.println("Enter the name of the pizza: ");
			pizzaName = in.nextLine();
			System.out.println("Enter the cost of the pizza: ");
			cost = in.nextFloat();
			System.out.println("Enter the quantity: ");
			quantity = in.nextInt();
			pizzaList.add(new Pizza(pizzaName, cost, quantity));
			sum += cost*quantity;
			System.out.println("Finish order 0 - yes, 1- no: ");
			finish = in.nextInt();
			if (finish == 1)
				in.nextLine();
		}
		System.out.println("Total amount $" + sum);		
		return new Order(name, address, pizzaList, sum);
	}
	
	public static void main(String[] args) throws IOException {
		MyQueue queue = new MyQueue();
		
		int ch = -1;
		while (ch != 0){
			ch = mainMenu();
			switch (ch){
				case 1:
					queue.push(insert()); 
					break;
				case 2:
					String filePath = "my_order.txt";
					queue.saveFile(filePath, queue.pop());
					break;
				case 3:
					queue.getQuantity();
					break;
				case 4:
					queue.show();
					break;
				default:
					if (ch != 0)
						System.out.println("Enter not correct number. Please try again\n");
					break;
				}
		}	
	}	
}
