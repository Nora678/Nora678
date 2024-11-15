import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Scanner;

class PhonebookAppDictionary {
    public static void main(String[] args) {
        Dictionary<String, Integer> d = new Hashtable<>();

        System.out.println("Welcome to the Contact List Phonebook Application! Please, select an item from the menu to proceed.");

        Scanner inp = new Scanner(System.in);
        boolean isbr = false;

        String name1, name2;
        Integer phone;

        while (!isbr) {
            System.out.println("_________________________________");
            System.out.println("1. Add a new contact.");
            System.out.println("2. Delete a contact.");
            System.out.println("3. Update a contact.");
            System.out.println("4. Display the contact list.");
            System.out.println("5. Display a person's phone number.");
            System.out.println("6. Exit.");

            int choice = inp.nextInt();
            inp.nextLine();

            System.out.println();

            switch (choice) {
                case 1:
                    System.out.println("Please, enter the name of the new contact.");
                    name1 = inp.nextLine();

                    System.out.println("Please, enter the phone number of the new contact.");
                    phone = inp.nextInt();
                    inp.nextLine(); 
                    d.put(name1, phone);
                    break;

                case 2:
                    System.out.println("Please, enter the name of the person whose contact you'd like to delete.");
                    name1 = inp.nextLine();
                    d.remove(name1);
                    break;

                case 3:
                    System.out.println("Please, enter the name of the person whose contact you'd like to change.");
                    name1 = inp.nextLine();
                    d.remove(name1);

                    System.out.println("Please, enter the updated name.");
                    name2 = inp.nextLine();

                    System.out.println("Please, enter the updated phone number.");
                    phone = inp.nextInt();
                    inp.nextLine(); 
                    d.put(name2, phone);
                    break;

                case 4:
                    System.out.println();
                    Enumeration<String> k = d.keys();
                    while (k.hasMoreElements()) {
                        String key = k.nextElement();
                        System.out.println(key + "   " + d.get(key));
                    }
                    break;

                case 5:
                    System.out.println("Please, enter the name of the person whose phone number you'd like to display.");
                    name1 = inp.nextLine();
                    Integer contactNumber = d.get(name1);
                    if (contactNumber != null) {
                        System.out.println(name1 + "'s phone number is: " + contactNumber);
                    } else {
                        System.out.println("Contact not found.");
                    }
                    break;

                case 6:
                    System.out.println("Thank you for using the Contact Dictionary! Have a lovely day!");
                    isbr = true;
                    break;

                default:
                    System.out.println("Invalid choice. Please select a number from the menu.");
            }
        }
    }
}
