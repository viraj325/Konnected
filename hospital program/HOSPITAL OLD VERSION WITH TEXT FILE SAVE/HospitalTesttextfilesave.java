//Viraj Patel

//8/3/2017 12.58am
//ADD A BOX FOR ID BECAUSE OF THE org.sqlite.SQLiteException: [SQLITE_CONSTRAINT_PRIMARYKEY]
// A PRIMARY KEY constraint failed (UNIQUE constraint failed: PATIENT.ID) ERROR

/*Program: This Program will store inputs(Name, Phone Number) into a array, will remove compartments of the information after the
patient has exited out of the place, will save the current arraylist into a file so the information gets saved, will read files
to retrieve the arraylist information and will be able to send messages to the patients entered phone number, giving them daily
and instant updates on the news. There will be prompts available to make it more efficient to send messages to multiple people
in a short amount of time. The prompts will be kept getting updated!!*/

import java.sql.*;
import java.text.DateFormat;
import java.util.*;
import java.io.*;
import java.awt.event.*; // for action events
import java.awt.*; // for layout managers
import javax.swing.*; // for GUI components
import javax.mail.*;
import javax.mail.internet.*;
import java.net.URI;
import java.util.Date;
import java.util.Timer;
import javax.xml.bind.DatatypeConverter;

public class HospitalTesttextfilesave extends JFrame
{
	//This method is to add data into the database
	public static void insertData(String fname, String lname, String number)throws SQLException
	{
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");
			String SQL = "INSERT INTO PATIENT VALUES (1, ?, ?, ?)";

			PreparedStatement pstmt = c.prepareStatement(SQL);
			pstmt.setString(1, fname);
			pstmt.setString(2, lname);
			pstmt.setString(3, number);

			pstmt.executeUpdate();
			pstmt.close();
			c.commit();
			c.close();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
		System.out.println("Records created successfully");
	}

	//This method is remove data from the database
	public static void deleteData(int num)
	{
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			String SQL = "DELETE FROM PATIENT WHERE ID=?;";
			PreparedStatement pstmt = c.prepareStatement(SQL);
			pstmt.setInt(1, num);
			pstmt.executeUpdate();
			pstmt.close();
			c.commit();
			c.close();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
		System.out.println("Operation done successfully");
	}

	//This ethod is to print the data from the database
	public static void printData(JTextArea printField)
	{
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery( "SELECT * FROM PATIENT;" );
			while ( rs.next() ) {
				int id = rs.getInt("ID");
				String  first = rs.getString("FIRST");
				String last  = rs.getString("LAST");
				String  phone = rs.getString("PHONE");
				System.out.println( "ID = " + id );
				System.out.println( "FIRST = " + first );
				System.out.println( "LAST = " + last );
				System.out.println( "PHONE = " + phone );
				System.out.println();
				printField.setText(first + "\n" + last + "\n" + phone + "\n\n");
			}
			rs.close();
			stmt.close();
			c.commit();
			c.close();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
		System.out.println("Operation done successfully");
	}

//This method is for saving the current arraylist into a textpad
	private static void saveInventory(ArrayList<hospitalInfo> nn)throws IOException
	{
		try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("Output.txt"))))
		{
			for(hospitalInfo b:nn)
			{
				String pn = b.getPatientName();
					out.print(DatatypeConverter.printBase64Binary(pn.getBytes())+ " ");
				String lpn = b.getLastPatientName();
					out.print(DatatypeConverter.printBase64Binary(lpn.getBytes()) + " ");
				String pp = (b.getPhoneNumber());
					out.print(DatatypeConverter.printBase64Binary(pp.getBytes()) + " ");
			}
		}
		catch (IOException e)
		{
			System.out.println(e);
		}
	}

	private static void saveAccount(String a, String b)throws IOException
	{
		try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("QWNjb3VudA=="))))
		{
			out.print(DatatypeConverter.printBase64Binary(a.getBytes()) + " ");
			out.print(DatatypeConverter.printBase64Binary(b.getBytes()) + " ");
		}
		catch (IOException e)
		{
			System.out.println(e);
		}
	}

//This method is for reminding the user monthly to update the password from the owner in order for the application to work
	/*public static void monthlyReminder(Frame frame, String s,String user, String pass) throws IOException {
		for(int month = 1; month <= 12; month++)
		{
			String year = s.substring(s.length() - 2);//Going to take the last 2 digits of the year
			int ye = Integer.parseInt(year);
			String checkDate = month + "/01/" + ye;
			if(s.equals(checkDate))
			{
				JOptionPane.showMessageDialog(frame, "Monthly Reminder");
				pass = "";
				saveAccount(user, pass);
				break;
			}
		}
	}*/

	//This method is to print out the prompts on textArea promptField
	private static void printPrompt(ArrayList<String> messageList, JTextArea promptField)
	{
		promptField.setText("");
		int a = 0;
		for(String i : messageList)
		{
			promptField.append((a + 1) + ": " + i + "\n\n");
			a++;
		}
	}

	private static void openUriLink()throws Exception
	{
		// Create Desktop object
		Desktop d=Desktop.getDesktop();

		// Browse a URL, say google.com
		d.browse(new URI("https://mail.google.com/mail/u/virajpatel325.com"));
	}

	private static void messagePerson(Frame frame, String username, String password, String to, String from,String subject, String text)//, String fileChooser
	{
		// sending email through google's gateaway
		String host = "smtp.gmail.com";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", "587");

		// Get the Session object.
		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});

		try {
			// Create a default MimeMessage object.
			Message message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(to));

			// Set Subject: header field
			message.setSubject(subject);

			// Now set the actual message
			message.setText(text);

			// Send message
			Transport.send(message);

			JOptionPane.showMessageDialog(frame, "Sent message successfully....");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	//This method is for the starting up screen logo
	public static void startup(JFrame start)
	{
		start.setTitle("Welcome");

		start.setLayout(new FlowLayout());

		ImageIcon image = new ImageIcon("startup.jpg");

		JLabel label = new JLabel("", image, JLabel.CENTER);

		JPanel panel = new JPanel(new BorderLayout());
			panel.add( label, BorderLayout.CENTER );

		start.add(panel);
		start.pack();
		start.setVisible(true);
		start.setIconImage(new ImageIcon("startup.jpg").getImage());
	}

//*****************MAIN METHOD**********************
	public static void main(String [] args)
	{
		Connection c = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
		System.out.println("Opened database successfully");

		JFrame start = new JFrame();
		JFrame frame = new JFrame();
		JFrame sett = new JFrame();
		startup(start);

		Date cal = new Date();
		DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.US);
		String s = df.format(cal);

		//This arraylist consists of the useful prompts which will be shown in the textArea in middle
		ArrayList<String> messageList = new ArrayList<>();
			messageList.add("Welcome to Patient Update Program, we will be updating you " +
					"\nabout the patient, " + "feel free to reply back with any questions you might have!");
			messageList.add("Patient checked in.");
			messageList.add("Patient's reports are normal.");
			messageList.add("Please rate this service out of 10 and feel free to give " +
					"\nus any feedback you have!");

		ArrayList<hospitalInfo> Info = new ArrayList<hospitalInfo>();
		final String[] fileChooser = {""};

		//THIS READS THE ACCOUNT FILE
		Scanner passput = null;
		File passfile = new File("QWNjb3VudA==");
		try{
			passput = new Scanner(passfile);
		} catch (FileNotFoundException e){
			JOptionPane.showMessageDialog(frame, "*****Cannot Find the Data File*****");
		}
		String a = "";
		String p = "";
		if(passfile.length() > 0)
		{
			a = passput.next();
				a = new String(DatatypeConverter.parseBase64Binary(a));
			p = passput.next();
				p = new String(DatatypeConverter.parseBase64Binary(p));
		}
		final String[] aun = {a};
		final String[] ap = {p};

		//*******************************|||||*****************************
		//THIS READS THE INVENTORY FILE
		Scanner input = null;
		final File[] file = {new File("Output.txt")};
		try {
			input = new Scanner(file[0]);
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(frame, "*****Cannot Find the Data File*****");
		}
		String fn;
		String ln;
		String en;
		if(file[0].length() > 0)
		{
			do {
				fn = input.next();
					String dpn = new String(DatatypeConverter.parseBase64Binary(fn));
				ln = input.next();
					String dlpn = new String(DatatypeConverter.parseBase64Binary(ln));
				en = input.next();
					String dpp = new String(DatatypeConverter.parseBase64Binary(en));

				Info.add(new hospitalInfo(dpn, dlpn, dpp));
			}
			while (input.hasNext());
		}

		//This timer will close the startup method in x amount of seconds and make the main frame visible
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				start.dispatchEvent(new WindowEvent(start, WindowEvent.WINDOW_CLOSING));
				frame.setVisible(true);
				/*try {
					monthlyReminder(frame, s, aun[0], ap[0]);
				} catch (IOException e) {
					e.printStackTrace();
				}*/
			}
		}, 1500);

		frame.setSize(new Dimension(1000,700));
		frame.setTitle("Konnected");
		frame.setLayout(new BorderLayout());

		JLabel last = new JLabel("Last: ");
			last.setForeground(Color.RED);
		JLabel first = new JLabel("First: ");
			first.setForeground(Color.RED);
		JLabel phone = new JLabel("Phone Number: ");
			phone.setForeground(Color.RED);
		JLabel patient = new JLabel("Patient #: ");
			patient.setForeground(Color.BLUE);
		JLabel carrier = new JLabel("Carrier: ");
			carrier.setForeground(Color.ORANGE);
		JLabel copyRight = new JLabel("Copyright © VIRAJ PATEL 2017 ALL RIGHTS RESERVED");
			copyRight.setFont(new Font(copyRight.getName(), Font.PLAIN, 8));
		JLabel date = new JLabel(s);
			date.setFont(new Font(date.getName(), Font.BOLD, 20));

		JTextField firstA = new JTextField(10);
		JTextField lastA = new JTextField(10);
		JTextField phoneA = new JTextField(10);
		JTextField patientA = new JTextField(2);
		JTextField carrierA = new JTextField(8);

		JButton add = new JButton("Add");
			add.setBackground(Color.RED);
		JButton remove = new JButton("Remove");
			remove.setBackground(Color.BLUE);
		JButton save = new JButton("Save");
		JButton message = new JButton("Message");
			message.setBackground(Color.BLUE);
		JButton print = new JButton("Print/Refresh");
		JButton help = new JButton("Help/Tips");
			help.setBackground(Color.YELLOW);
		JButton openLink = new JButton("View");
			openLink.setBackground(Color.MAGENTA);
		JButton account = new JButton("Account");
			account.setBackground(Color.ORANGE);
				JLabel userN = new JLabel("Username(Type in the part before @email.com)");
					JTextField usernA = new JTextField(10);
				JLabel userP = new JLabel("Password");
					JTextField userpA = new JTextField(10);
					JButton update = new JButton("Update");
						update.setBackground(Color.ORANGE);

		JTextArea messageField = new JTextArea(15, 35);
		JTextArea printField = new JTextArea(15, 15);
			printField.setEditable(false);
		JTextArea promptField = new JTextArea(15,40);
			promptField.setEditable(false);

		frame.setLayout(new FlowLayout());

		frame.add(first);
		frame.add(firstA);
		frame.add(last);
		frame.add(lastA);
		frame.add(phone);
		frame.add(phoneA);
		frame.add(add);
		frame.add(patient);
		frame.add(patientA);
		frame.add(remove);
		frame.add(save);
		frame.add(print);
		frame.add(carrier);
		frame.add(carrierA);
		frame.add(message);
		frame.add(openLink);
		frame.add(new JScrollPane(printField));
		frame.add(new JScrollPane(promptField));
		frame.add(messageField);
		frame.add(help);
		frame.add(account);
		frame.add(date);
		frame.add(copyRight, BorderLayout.SOUTH);

		printPrompt(messageList, promptField);

		//This action listener will allow the user to change or update the account information
		account.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sett.setVisible(true);
				sett.setLayout(new FlowLayout());
				sett.setSize(700,100);

				sett.add(userN);
				sett.add(usernA);
				sett.add(userP);
				sett.add(userpA);
				sett.add(update);
					update.addActionListener(e12 -> {
                        aun[0] = usernA.getText();
                        ap[0] = userpA.getText();
                        try {
                        	if(aun[0].length()>0&&ap[0].length()>0) {
								saveAccount(aun[0], ap[0]);
							}
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        usernA.setText("");
                        userpA.setText("");
                    });
			}
		});

		//This button action listener will open up the gmail account on a default web browser
		openLink.addActionListener(e -> {
            try {
                openUriLink();
            } catch (Exception e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(frame, "The System Crashed", "Link Crash", JOptionPane.ERROR_MESSAGE);
            }
        });

		//This action listener will ope up a jframe with tips and advices on how to use the application
		help.addActionListener(ae -> JOptionPane.showMessageDialog(frame, "****MAKE SURE YOU CLICK READ THE FIRST THING WHEN YOU START THE APPLICATION" +
                "\n****Click Read only if you have saved data from previous use or " +
                "otherwise it will crash." +
                "\n1: Add the correct information in the corresponding boxes and than click \"Add\"." +
                "\n2: Click \"Print/Refresh\" to keep on updating the list as you make changes." +
                "\n3: Click Save when you want to save all the changes you have made." +
                "\n4: Click Remove after entering the desired patient # provided in the \"Print/Refresh\" list." +
                "\n5: Make sure to enter the patient # and their phone carrier and some type of message before clicking " +
                "message otherwise it will crash." +
                "\n6: There are useful prompts in the window to the left of messages, just type in the prompt" +
                " number you want to use and click message to send the message."));

		//This action listener will add the information into the hospitalInfo array Info
		add.addActionListener(ae -> {
            String firstName = firstA.getText();
            String lastName = lastA.getText();
            String phoneNumber = phoneA.getText();

            if(phoneA.getText().matches("[0-9]+"))
            {
                if (phoneA.getText().length() < 10)
                {
                    JOptionPane.showMessageDialog(frame, "The number seems to be incorrect, please try again.", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
                else
                {
                    Info.add(new hospitalInfo(firstName, lastName, phoneNumber));
                    try {
                        insertData(firstName, lastName, phoneNumber);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            //this is for emails
            else
            {
                Info.add(new hospitalInfo(firstName, lastName, phoneNumber));
                try {
                    insertData(firstName, lastName, phoneNumber);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            firstA.setText("");
            lastA.setText("");
            phoneA.setText("");
        });

		//This action listener will remove the desired compartment from the array Info
		remove.addActionListener((ActionEvent ae) -> {
            if(patientA.getText().length() > 0)
            {
                int removeNum = Integer.parseInt(patientA.getText());
                //removeNum = removeNum - 1;

                //Info.remove(removeNum);
                deleteData(removeNum);

                patientA.setText("");
            }
            else
            {
                JOptionPane.showMessageDialog(frame, "Please ENTER the patient # and then continue!", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

		//This action listener will simply call saveInventory method to save the data
		save.addActionListener(ae -> {
            try
            {
                saveInventory(Info);
            }
            catch (Exception a12)
            {
                a12.printStackTrace();
            }
        });

		//This action listener will print the information from the array Info to a jpanel
		print.addActionListener(ae -> {
            /*printField.setText("");
            int a1 = 0;
            for(hospitalInfo i : Info)
            {
                printField.append("Patient #: " + (a1 + 1) + "\n" + i);
                a1++;
            }*/
            printData(printField);
        });

//This actionListener will call the messaging method to send messages
		message.addActionListener(ae -> {
            int messageNum = Integer.parseInt(patientA.getText());
                messageNum = messageNum - 1;

            String n = Info.get(messageNum).getPhoneNumber();

            //This loop is for deciding which service the email is going to have to use in order to send the email
            String carrier1 = null;
                if(carrierA.getText().equalsIgnoreCase("Sprint"))
                    carrier1 = "@messaging.sprintpcs.com";
                else if(carrierA.getText().equalsIgnoreCase("Verizon"))
                    carrier1 ="@vtext.com";
                else if(carrierA.getText().equalsIgnoreCase("TMobile"))
                    carrier1 = "@tmomail.net";
                else if(carrierA.getText().equalsIgnoreCase("ATT"))
                    carrier1 = "@txt.att.net";
                else
                    JOptionPane.showMessageDialog(frame, "Please enter the service provider of the phone number", "Carrier Error", JOptionPane.ERROR_MESSAGE);

                //This loop checks if the textArea messageField has any numbers at the beginning and if it does than it gets the prompt from the same number compartment.
                if(messageField.getText().matches("[0-9]+"))
                {
                    int i = Integer.parseInt(messageField.getText());
                        i = i - 1;
                    messageField.setText(messageList.get(i));
                }

            // Recipient's email ID needs to be mentioned.
            String to = n + carrier1;//change accordingly

            // Sender's email ID needs to be mentioned
            String from = "bruce.pijanowski@gmail.com";//change accordingly
            final String username = aun[0];//change accordingly
            final String password = ap[0];//change accordingly
            String subject = "WARNING";
            String text = messageField.getText();

            messagePerson(frame, username, password, to, from, subject, text);//, fileChooser[0]

            messageField.setText("");
            patientA.setText("");
            carrierA.setText("");
        });

		//This window listener will automatically save all the data when the program closes
		//If this does not work add or delete java.awt.event.WindowEvent windowEvent in windowClosing()
		frame.addWindowListener(new java.awt.event.WindowAdapter()
		{
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				if(Info.size()>0)
				{
					try
					{
						saveInventory(Info);
					}
					catch (Exception a)
					{
						a.printStackTrace();
					}
					System.exit(0);
				}
			}
		});

		sett.setIconImage(new ImageIcon("Konnected.jpg").getImage());
		frame.setIconImage(new ImageIcon("Konnected.jpg").getImage());
		frame.pack();
		frame.setVisible(false);
		sett.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		start.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
}