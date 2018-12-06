import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.client.*;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.mongodb.client.model.Aggregates.group;
import static com.mongodb.client.model.Aggregates.out;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;
import static com.mongodb.client.model.Indexes.descending;
import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;
import static com.mongodb.client.model.Sorts.ascending;
import static jdk.nashorn.internal.objects.NativeArray.sort;

import com.mongodb.client.model.Aggregates.*;

/*

  Compile:
  javac -cp mongo-java-driver-3.4.3.jar EstablishConnection.java

  Run:
  java -cp mongo-java-driver-3.4.3.jar:. EstablishConnection

*/

//Add full time to the studnet.csv

public class EstablishConnection {

    public static void menu() {
        System.out.println("----------------------Program Menu----------------------");
        System.out.println("1. Insert");
        System.out.println("2. Search");
        System.out.println("3. Print all documents from specific collection");
        System.out.println("4. Update");
        System.out.println("5. Delete");
        System.out.println("6. Aggregation (Sort)");
        System.out.println("7. Exit");
        System.out.println("--------------------------------------------------------");
    }


    public static void main(String args[]) {
        menu();
        Scanner input = new Scanner(System.in);
        int user_input = input.nextInt();
        List<String> array = new ArrayList<String>();
        Map<String, Object> languagesMapDetail = new HashMap<String, Object>();

        //Set all database variables
        String server_name = "localhost";
        int port = 27017;
        String database_name = "Techx";
        String collection_class = "classes";
        String collection_Mentor = "mentors";
        String collection_Student = "students";

        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);

        //Connect to Mongodb Instance
        MongoClient mongoClient = new MongoClient(server_name, port);
        MongoDatabase database = mongoClient.getDatabase(database_name);


        //collection for classes
        MongoCollection<Document> collectionClass = database.getCollection(collection_class);
        //collection for mentors
        MongoCollection<Document> collectionMentor = database.getCollection(collection_Mentor);
        //collection for students
        MongoCollection<Document> collectionStudent = database.getCollection(collection_Student);

        //Iterator for classes
        MongoCursor<Document> classes = collectionClass.find().iterator();
        //Iterator for mentors
        MongoCursor<Document> mentors = collectionMentor.find().iterator();
        //Iterator for students
        MongoCursor<Document> students = collectionStudent.find().iterator();

        //Query to find a document through multiple fields
        BasicDBObject query = new BasicDBObject();

//         Document myDoc = collectionClass.find().first();
//         System.out.println(myDoc.toJson());

        //FindIterable<Document> cursor = collectionClass.find();


        while (user_input != 7) {
            if (user_input == 1) {
                System.out.println("Choose collection[1: Classes 2: Mentors 3: Students]");
                int choice = input.nextInt();
                switch (choice) {
                    case 1:
                        System.out.println("Enter the values for the following Categories: ");
                        // Skip the newline
                        input.nextLine();

                        System.out.println("Course:");
                        String courseNum = input.nextLine();


                        System.out.println("Class Name:");
                        String className = input.nextLine();

                        System.out.println("Instructor:");
                        String instructor = input.nextLine();

                        Document newClass = new Document("Course Number", courseNum)
                                .append("Class Name", className)
                                .append("Instructor", instructor);

                        collectionClass.insertOne(newClass);
                        System.out.println(className + " was added to classes!");
                        break;
                    case 2:
                        System.out.println("Enter the values for the following Categories: ");
                        // Skip the newline
                        input.nextLine();

                        System.out.println("First Name:");
                        String firstName = input.nextLine();

                        System.out.println("Last Name:");
                        String lastName = input.nextLine();

                        System.out.println("Home State, format[\"CA\"]:");
                        String state = input.nextLine();

                        System.out.println("Undergraduate Alma Mater:");
                        String underGradalmaMater = input.nextLine();

                        System.out.println("Graduate Alma Mater:");
                        String gradAlmaMater = input.nextLine();

                        System.out.println("Thesis Focus:");
                        String thesis = input.nextLine();

                        System.out.println("Tech Interest:");
                        String interest = input.nextLine();

                        System.out.println("Department/Team:");
                        String department = input.nextLine();

                        System.out.println("Role:");
                        String role = input.nextLine();

                        System.out.println("How many years have you worked at Google?");
                        int years = input.nextInt();

                        System.out.println("Mentee(s):");
                        String mentees = input.nextLine();

                        Document newMentor = new Document("First Name", firstName)
                                .append("Last Name", lastName)
                                .append("Home State", state)
                                .append("Undergraduate Alma Mater", underGradalmaMater)
                                .append("Graduate Alma Mater", gradAlmaMater)
                                .append("Thesis Focus", thesis)
                                .append("Tech Interest", interest)
                                .append("Department/Team", department)
                                .append("Role", role)
                                .append("How many years have your worked at Google?", years)
                                .append("Mentee(s)", mentees);

                        collectionMentor.insertOne(newMentor);
                        System.out.println(firstName + " " + lastName + " was added to mentors!");
                        break;
                    case 3:
                        System.out.println("Enter the values for the following Categories: ");
                        // Skip the newline
                        input.nextLine();

                        System.out.println("First Name:");
                        String studentFirstName = input.nextLine();

                        System.out.println("Last Name:");
                        String studentLastName = input.nextLine();

                        System.out.println("School:");
                        String school = input.nextLine();

                        System.out.println("How many classes are you taking?:");
                        int classNum = input.nextInt();
                        System.out.println("Enter the classes names:");
                        // Skip the newline
                        input.nextLine();
                        for (int i = 0; i < classNum; i++) {
                            System.out.println("Class " + i + ":");
                            String classesTaking = input.nextLine();
                            array.add(classesTaking);
                        }

                        System.out.println("Home State, format[\"CA\"]:");
                        String studentState = input.nextLine();

                        System.out.println("Enter the following level of knowledge for the following languages [Beginner, Intermediate, Expert, N/A]");
                        String[] language = {"Java", "Python", "C++", "HTML/CSS", "Javascript"};
                        for (int i = 0; i < 5; i++) {
                            System.out.println("Level of knowledge for " + language[i] + ":");
                            String level = input.nextLine();
                            languagesMapDetail.put(language[i], level);
                        }

                        System.out.println("Horoscope:");
                        String horoscope = input.nextLine();

                        System.out.println("Favorite Cuisine:");
                        String cuisine = input.nextLine();

                        System.out.println("Type of Diet:");
                        String diet = input.nextLine();

                        System.out.println("Favorite Music Genre:");
                        String music = input.nextLine();

                        boolean fulltime;
                        if (array.size() >= 4) {
                            fulltime = true;
                        } else fulltime = false;

                        Document newStudent = new Document("First Name", studentFirstName)
                                .append("Last Name", studentLastName)
                                .append("School", school)
                                .append("Current Classes", array)
                                .append("Home State", studentState)
                                .append("Programming Languages", languagesMapDetail)
                                .append("Horoscope", horoscope)
                                .append("Favorite Cuisine", cuisine)
                                .append("Type of Diet", diet)
                                .append("Favorite Music Genre", music)
                                .append("Full Time", fulltime);

                        collectionStudent.insertOne(newStudent);
                        System.out.println(studentFirstName + " " + studentLastName + " was added to students!");
                        break;
                }
                menu();
                user_input = input.nextInt();

            } else if (user_input == 2) {
                System.out.println("Choose collection[1: Classes 2: Mentors 3: Students]");
                int choice = input.nextInt();
                //skip line
                input.nextLine();
                switch (choice) {
                    case 1:
                        System.out.println("Search by? [Course Number, Class Name, Instructor]");
                        String selection = input.nextLine();
                        System.out.println("What is the specific value for " + selection + ":");
                        String value = input.nextLine();
                        List<Document> listObjects = collectionClass.find(eq(selection, value)).into(new ArrayList<Document>());
                        for (Document aList : listObjects) {
                            System.out.println(aList);
                        }
                        Document myDoc = Document.parse(collectionClass.find(eq(selection, value)).toString());
                        //System.out.println(myDoc.toJson() + "\n");
                        break;
                    case 2:
                        System.out.println("Search by? [First Name, Last Name, Home State, Undergraduate Alma Mater, Graduate Alma Mater,\n" +
                                " Thesis Focus, Tech Interest, Department/Team, Role, How many years have you worked at Google?, Mentee(s)]");
                        selection = input.nextLine();
                        System.out.println("What is the specific value for " + selection + ":");
                        value = input.nextLine();
                        listObjects = collectionMentor.find(eq(selection, value)).into(new ArrayList<Document>());
                        for (Document aList : listObjects) {
                            System.out.println(aList + "\n\n");
                        }
                        //myDoc = collectionMentor.find(eq(selection, value)).first();
                        //System.out.println(myDoc.toJson() + "\n");
                        break;
                    case 3:
                        System.out.println("Search by? [First Name, Last Name, School, Current Classes, Home State,\n" +
                                "Programming Languages, Horoscope, Favorite Cuisine, Type of Diet, Favorite Music Genre]");
                        selection = input.nextLine();
                        System.out.println("What is the specific value for " + selection + ":");
                        if (selection.equals("Programming Languages")) {
                            String language = input.nextLine();
                            System.out.println("What level of skill are you looking for?");
                            String knowledge = input.nextLine();
                            listObjects = collectionStudent.find(eq(selection + "." + language, knowledge)).into(new ArrayList<Document>());
                            for (Document aList : listObjects) {
                                System.out.println(aList);
                            }
                            //myDoc = collectionStudent.find(eq(selection + "." + language, knowledge)).first();
                            //System.out.println(myDoc.toJson() + "\n");
                        } else if(selection.equals("Current Classes")) {
                            System.out.println("What class are you looking for?");
                            String classSearch = input.nextLine();
                            myDoc = collectionStudent.find(eq(selection + ".", new BasicDBObject("$in",Arrays.asList(classSearch)))).first();
                            System.out.println(myDoc);
                        }else {
                            value = input.nextLine();
                            listObjects = collectionStudent.find(eq(selection, value)).into(new ArrayList<Document>());
                            for (Document aList : listObjects) {
                                System.out.println(aList);
                            }
                            //myDoc = collectionStudent.find(eq(selection, value)).first();
                            //System.out.println(myDoc.toJson() + "\n");
                        }
                        break;

                }
                menu();
                user_input = input.nextInt();

            } else if (user_input == 3) {
                System.out.println("Choose collection[1: Classes 2: Mentors 3: Students]");
                int choice = input.nextInt();
                switch (choice) {
                    case 1:
                        System.out.print("Classes: ");
                        try {
                            while (classes.hasNext()) {
                                System.out.println(classes.next().toJson());
                            }
                        } finally {
                            System.out.println("Done");
                        }
                        break;
                    case 2:
                        System.out.print("Mentors: ");
                        try {
                            while (mentors.hasNext()) {
                                System.out.println(mentors.next().toJson());
                            }
                        } finally {
                            System.out.println("Done");
                        }
                        break;
                    case 3:
                        System.out.print("Students: ");
                        try {
                            while (students.hasNext()) {
                                System.out.println(students.next().toJson());
                            }
                        } finally {
                            System.out.println("Done");
                        }
                        break;
                }
                menu();
                user_input = input.nextInt();

            } else if (user_input == 4) {
                System.out.println("Choose collection[1: Classes 2: Mentors 3: Students]");
                int choice = input.nextInt();
                // Skip the newline
                input.nextLine();
                switch (choice) {
                    case 1:
                        System.out.println("What value would you like to update? [Course Number, Class Name, Instructor]");
                        String updateValue = input.nextLine();
                        // Skip the newline
                        input.nextLine();
                        String currentChangeValue;
                        input.nextLine();
                        String updatedChangeValue;

                        if (updateValue.equals("Course Number")) {
                            System.out.println("Whats the current course number?");
                            currentChangeValue = input.nextLine();
                            System.out.println("Enter the new course number: ");
                            updatedChangeValue = input.nextLine();
                            collectionClass.updateOne(eq("Course Number", currentChangeValue), new Document("$set", new Document("Course Number", updatedChangeValue)));
                            System.out.println("Course Number updated!");

                        } else if (updateValue.equals("Class Name")) {
                            System.out.println("Whats the current class name?");
                            currentChangeValue = input.nextLine();
                            System.out.println("Enter the new class name: ");
                            updatedChangeValue = input.nextLine();
                            collectionClass.updateOne(eq("Class Name", currentChangeValue), new Document("$set", new Document("Class Name", updatedChangeValue)));
                            System.out.println("Class Name updated!");

                        } else if (updateValue.equals("Instructor")) {
                            System.out.println("Whats the current Instructor?");
                            currentChangeValue = input.nextLine();
                            System.out.println("Enter the new instructors name: ");
                            updatedChangeValue = input.nextLine();
                            collectionClass.updateOne(eq("Instructor", currentChangeValue), new Document("$set", new Document("Instructor", updatedChangeValue)));
                            System.out.println("Class Instructor updated!");
                        } else {
                            System.out.println("***No valid matches***");
                        }
                        break;
                    case 2:
                        System.out.println("What value would you like to update? \n " +
                                "[First Name, Last Name, Home State, Undergraduate Alma Mater, Graduate Alma Mater,\n" +
                                " Thesis Focus, Tech Interest, Department/Team, Role, How many years have your worked at Google?, Mentee(s)]");
                        updateValue = input.nextLine();

                        if (updateValue.equals("First Name")) {
                            System.out.println("Whats the current first name?");
                            currentChangeValue = input.nextLine();
                            System.out.println("Enter the new first name: ");
                            updatedChangeValue = input.nextLine();
                            collectionMentor.updateOne(eq("First Name", currentChangeValue), new Document("$set", new Document("First Name", updatedChangeValue)));

                        } else if (updateValue.equals("Last Name")) {
                            System.out.println("Whats the current last name?");
                            currentChangeValue = input.nextLine();
                            System.out.println("Enter the new first name: ");
                            updatedChangeValue = input.nextLine();
                            collectionMentor.updateOne(eq("Last Name", currentChangeValue), new Document("$set", new Document("Last Name", updatedChangeValue)));

                        } else if (updateValue.equals("Home State")) {
                            System.out.println("Whats the current home state?");
                            currentChangeValue = input.nextLine();
                            System.out.println("Enter the new home state: ");
                            updatedChangeValue = input.nextLine();
                            collectionMentor.updateOne(eq("Home State", currentChangeValue), new Document("$set", new Document("Home State", updatedChangeValue)));
                        } else if (updateValue.equals("Undergraduate Alma Mater")) {
                            System.out.println("Whats your current Undergraduate Alma Mater?");
                            currentChangeValue = input.nextLine();
                            System.out.println("Enter the new Undergraduate Alma Mater: ");
                            updatedChangeValue = input.nextLine();
                            collectionMentor.updateOne(eq("Undergraduate Alma Mater", currentChangeValue), new Document("$set", new Document("Undergraduate Alma Mater", updatedChangeValue)));
                        } else if (updateValue.equals("Graduate Alma Mater")) {
                            System.out.println("Whats your current Graduate Alma Mater?");
                            currentChangeValue = input.nextLine();
                            System.out.println("Enter the new Graduate Alma Mater: ");
                            updatedChangeValue = input.nextLine();
                            collectionMentor.updateOne(eq("Graduate Alma Mater", currentChangeValue), new Document("$set", new Document("Graduate Alma Mater", updatedChangeValue)));
                        } else if (updateValue.equals("Thesis Focus")) {
                            System.out.println("Whats your current thesis focus?");
                            currentChangeValue = input.nextLine();
                            System.out.println("Enter the new thesis focus: ");
                            updatedChangeValue = input.nextLine();
                            collectionMentor.updateOne(eq("Thesis Focus", currentChangeValue), new Document("$set", new Document("Thesis Focus", updatedChangeValue)));
                        } else if (updateValue.equals("Tech Interest")) {
                            System.out.println("Whats your current tech interest?");
                            currentChangeValue = input.nextLine();
                            System.out.println("Enter the new tech interest: ");
                            updatedChangeValue = input.nextLine();
                            collectionMentor.updateOne(eq("Tech Interest", currentChangeValue), new Document("$set", new Document("Tech Interest", updatedChangeValue)));
                        } else if (updateValue.equals("Department/Team")) {
                            System.out.println("Whats your current department/team?");
                            currentChangeValue = input.nextLine();
                            System.out.println("Enter the new department/team: ");
                            updatedChangeValue = input.nextLine();
                            collectionMentor.updateOne(eq("Department/Team", currentChangeValue), new Document("$set", new Document("Department/Team", updatedChangeValue)));
                        } else if (updateValue.equals("Role")) {
                            System.out.println("Whats your current role?");
                            currentChangeValue = input.nextLine();
                            System.out.println("Enter the new role: ");
                            updatedChangeValue = input.nextLine();
                            collectionMentor.updateOne(eq("Role", currentChangeValue), new Document("$set", new Document("Role", updatedChangeValue)));
                        } else if (updateValue.equals("How many years have your worked at Google?")) {
                            System.out.println("How many years have you currently work at Google?");
                            currentChangeValue = input.nextLine();
                            System.out.println("Enter new number of years: ");
                            updatedChangeValue = input.nextLine();
                            collectionMentor.updateOne(eq("How many years have your worked at Google?", currentChangeValue), new Document("$set", new Document("How many years have your worked at Google?", updatedChangeValue)));
                        } else if (updateValue.equals("Mentee(s)")) {
                            System.out.println("Who are your current mentee(s)?");
                            currentChangeValue = input.nextLine();
                            System.out.println("Enter new mentee(s): ");
                            updatedChangeValue = input.nextLine();
                            collectionMentor.updateOne(eq("Mentee(s)", currentChangeValue), new Document("$set", new Document("Mentee(s)", updatedChangeValue)));

                        } else {
                            System.out.println("***No valid matches***");
                        }
                        break;
                    case 3:
                        System.out.println("What value would you like to update? \n " +
                                "[First Name, Last Name, School, Current Classes, Home State,\n" +
                                " Programming Languages, Horoscope, Favorite Cuisine, Type of Diet, Favorite Music Genre, Full Time]");
                        updateValue = input.nextLine();
                        if (updateValue.equals("First Name")) {
                            System.out.println("Whats the current first name?");
                            currentChangeValue = input.nextLine();
                            System.out.println("Enter the new first name: ");
                            updatedChangeValue = input.nextLine();
                            collectionStudent.updateOne(eq("First Name", currentChangeValue), new Document("$set", new Document("First Name", updatedChangeValue)));

                        } else if (updateValue.equals("Last Name")) {
                            System.out.println("Whats the current last name?");
                            currentChangeValue = input.nextLine();
                            System.out.println("Enter the new first name: ");
                            updatedChangeValue = input.nextLine();
                            collectionStudent.updateOne(eq("Last Name", currentChangeValue), new Document("$set", new Document("Last Name", updatedChangeValue)));
                        } else if (updateValue.equals("School")) {
                            System.out.println("Who's school are you updating?");
                            currentChangeValue = input.nextLine();
                            System.out.println("Enter the new school: ");
                            updatedChangeValue = input.nextLine();
                            collectionStudent.updateOne(eq("First Name", currentChangeValue), new Document("$set", new Document("School", updatedChangeValue)));
                        } else if (updateValue.equals("Current Classes")) {
                            System.out.println("Who's classes are you updating?");
                            currentChangeValue = input.nextLine();
                            System.out.println("Enter the new classes");
                            updatedChangeValue = input.nextLine();
                            String[] arrayOfClasses = updatedChangeValue.split(",");
                            collectionStudent.updateOne(eq("First Name", currentChangeValue), new Document("$set", new Document("Current Classes", Arrays.toString(arrayOfClasses))));
                        } else if (updateValue.equals("Home State")) {
                            System.out.println("Who's home state are you updating?");
                            currentChangeValue = input.nextLine();
                            System.out.println("Enter " + currentChangeValue + "'s new home state:");
                            updatedChangeValue = input.nextLine();
                            collectionStudent.updateOne(eq("First Name", currentChangeValue), new Document("$set", new Document("Home State", updatedChangeValue)));
                        } else if (updateValue.equals("Programming Languages")) {
                            System.out.println("Who's programming languages are you updating?");
                            currentChangeValue = input.nextLine();
                            System.out.println("Which programming language do you want to update? [Java, Python, C++, HTML/CSS, Javascript]");
                            String language = input.nextLine();
                            System.out.println("What is " + currentChangeValue + "'s new level of knowledge for " + language + "?");
                            String levelOfKnowledge = input.nextLine();
                            collectionStudent.updateOne(eq("First Name", currentChangeValue), new Document("$set", new Document("Programming Languages." + language, levelOfKnowledge)));
                        } else if (updateValue.equals("Horoscope")) {
                            System.out.println("Who's horoscope are you updating?");
                            currentChangeValue = input.nextLine();
                            System.out.println("What is " + currentChangeValue + "'s new horoscope");
                            updatedChangeValue = input.nextLine();
                            collectionStudent.updateOne(eq("First Name", currentChangeValue), new Document("$set", new Document("Horoscope", updatedChangeValue)));
                        } else if (updateValue.equals("Favorite Cuisine")) {
                            System.out.println("Who's favorite cuisine are you updating?");
                            currentChangeValue = input.nextLine();
                            System.out.println("What is " + currentChangeValue + "'s new favorite cuisine?");
                            updatedChangeValue = input.nextLine();
                            collectionStudent.updateOne(eq("First Name", currentChangeValue), new Document("$set", new Document("Favorite Cuisine", updatedChangeValue)));
                        } else if (updateValue.equals("Type of Diet")) {
                            System.out.println("Who's type of diet are you updating?");
                            currentChangeValue = input.nextLine();
                            System.out.println("What is " + currentChangeValue + "'s new type of diet?");
                            updatedChangeValue = input.nextLine();
                            collectionStudent.updateOne(eq("First Name", currentChangeValue), new Document("$set", new Document("Type of Diet", updatedChangeValue)));
                        } else if (updateValue.equals("Favorite Music Genre")) {
                            System.out.println("Who's favorite music genre are you updating?");
                            currentChangeValue = input.nextLine();
                            System.out.println("What is " + currentChangeValue + "'s new favorite music genre?");
                            updatedChangeValue = input.nextLine();
                            collectionStudent.updateOne(eq("First Name", currentChangeValue), new Document("$set", new Document("Favorite Music Genre", updatedChangeValue)));
                        } else {
                            System.out.println("***No valid matches***");
                        }
                        break;
                }
                menu();
                user_input = input.nextInt();

            } else if (user_input == 5) {
                System.out.println("Choose collection[1: Classes 2: Mentors 3: Students]");
                int choice = input.nextInt();
                String valueToDelete;
                // Skip the newline
                input.nextLine();
                switch (choice) {
                    case 1:
                        System.out.println("****Deleting from Classes collection****\n");
                        System.out.println("What class would you like to delete?");
                        valueToDelete = input.nextLine();
                        collectionClass.deleteOne(eq("Class Name", valueToDelete));
                        System.out.println(valueToDelete + " was deleted!");
                        break;
                    case 2:
                        System.out.println("****Deleting from Mentors collection****\n");
                        System.out.println("Enter the first name of the mentor who's data you would like to delete?");
                        String firstName = input.nextLine();
                        System.out.println("Enter last name:");
                        String lastName = input.nextLine();
                        query.append("First Name", firstName).append("Last Name", lastName);
                        collectionMentor.deleteOne(query);
                        System.out.println(firstName + " " + lastName + " was deleted!");
                        break;
                    case 3:
                        System.out.println("****Deleting from Students collection****\n");
                        System.out.println("Enter the first name of the student who's data you would like to delete?");
                        firstName = input.nextLine();
                        System.out.println("Enter last name:");
                        lastName = input.nextLine();
                        query.append("First Name", firstName).append("Last Name", lastName);
                        collectionStudent.deleteOne(query);
                        System.out.println(firstName + " " + lastName + " was deleted!");
                        break;
                }
                menu();
                user_input = input.nextInt();
            } else if (user_input == 6) {
                System.out.println("Choose collection[2: Mentors 3: Students]");
                int choice = input.nextInt();
                String valueToDelete;
                // Skip the newline
                input.nextLine();
                switch (choice) {
                    case 1:
                        break;
                    case 2:
                        System.out.println("Mentors years worked at google sorted in descending order:");
                        List<Document> list = collectionMentor.find().sort(descending("How many years have you worked at Google?")).into(new ArrayList<Document>());
                        for (Document aList : list) {
                            System.out.println(aList);
                        }
                        break;
                    case 3:
                        System.out.println("Students schools sorted in descending order:");
                        List<Document> list2 = collectionStudent.find().sort(descending("School")).into(new ArrayList<Document>());
                        for (Document aList : list2) {
                            System.out.println(aList);
                        }
                        break;
                }
                menu();
                user_input = input.nextInt();
            }
        }
    }

}