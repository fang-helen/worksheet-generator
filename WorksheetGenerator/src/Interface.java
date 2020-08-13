import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class Interface extends Application
{
	Stage stage;
	Scene worksheetSelections;
	TextField titleField;
	TextField numQsField;
	ComboBox<Integer> digits1;
	ComboBox<Integer> digits2;
	CheckBox useNegatives;
	RadioButton chooseOpButton;
	RadioButton randomOpButton;
	ComboBox<String> opSelection;
	
	ArrayList<String> errors;
	
	Stage errorPop;
	Scene errorScene;
	VBox errorBox;
	
	Scene wsScene;
	VBox wsBox;
	int viewCols = 2;
	
	Scene keyScene;
	VBox keyBox;
	
	Worksheet w;
	
	ClipboardContent clipboard;
	
	@Override
	public void start(Stage stage) throws Exception {
		this.stage = stage;
		
		clipboard = new ClipboardContent();
		
		errors = new ArrayList<String>();
		
		VBox box = new VBox();
		
		MenuBar homeMenu = new MenuBar();
		Menu homeOptions = new Menu("Options");
		homeMenu.getMenus().add(homeOptions);
		MenuItem homeStartOver = new MenuItem("Start Over");
		MenuItem homeQuit = new MenuItem("Quit");
		
		homeOptions.getItems().addAll(homeStartOver,homeQuit);
		homeStartOver.setOnAction(e->{
			resetScreen();
		});
		homeQuit.setOnAction(e -> {
			System.exit(0);
		});
		
		
		Label header = new Label("Worksheet Generator");
		header.setFont(Font.font("Ink Free",FontWeight.BOLD, 48));
		
		BorderPane headerBox = new BorderPane();
		headerBox.setCenter(header);
		headerBox.setPadding(new Insets(15));
		
		Line hLine = new Line(25,45,505,45);
		
		Label titleLabel = new Label("Title your worksheet (optional):");
		TextField titleField = new TextField();
		titleField.setPromptText("Enter a worksheet title...");
		this.titleField = titleField;
		
		HBox titleWSBox = new HBox();
		titleWSBox.getChildren().addAll(titleLabel,titleField);
		titleWSBox.setSpacing(35);
		
		Label numQLabel = new Label("Enter number of questions: ");
		TextField numQField = new TextField();
		numQField.setPromptText("Type a number...");
		numQsField = numQField;
		
		HBox numQBox = new HBox();
		numQBox.getChildren().addAll(numQLabel,numQField);
		numQBox.setSpacing(35);
		
		GridPane titleQsPane = new GridPane();
		titleQsPane.add(titleLabel, 0, 0);
		titleQsPane.add(titleField, 1, 0);
		titleQsPane.add(numQLabel, 0, 1);
		titleQsPane.add(numQField, 1, 1);
		titleQsPane.setVgap(30);
		titleQsPane.setHgap(50);
		
		Label numDigitsLabel = new Label("Enter number of digits to work with: ");
		Label digits1Label = new Label("First number:");
		Label digits2Label = new Label("Second number:");
		ComboBox<Integer> digits1 = new ComboBox<Integer>();
		digits1.getItems().addAll(1,2,3,4,5,6,7,8);
		digits1.setPrefWidth(85);
		digits1.setValue(0);
		ComboBox<Integer> digits2 = new ComboBox<Integer>();
		digits2.getItems().addAll(1,2,3,4,5,6,7,8);
		digits2.setPrefWidth(85);
		digits2.setValue(0);
	
				
		GridPane numDigitsBox = new GridPane();
		numDigitsBox.add(digits1Label, 1, 0);
		numDigitsBox.add(digits2Label, 2, 0);
		numDigitsBox.add(numDigitsLabel, 0, 1);
		numDigitsBox.add(digits1, 1, 1);
		numDigitsBox.add(digits2, 2, 1);
		numDigitsBox.setHgap(15);
		numDigitsBox.setVgap(5);
		
		this.digits1 = digits1;
		this.digits2 = digits2;
		
		CheckBox useNegatives = new CheckBox("Use negatives?");
		this.useNegatives = useNegatives;

		HBox chooseOpBox = new HBox();
		RadioButton chooseOp = new RadioButton("Specify an operation: ");
		ComboBox<String> opSelection = new ComboBox<String>();
		opSelection.getItems().addAll("Addition","Subtraction","Multiplication","Division; no remainders","Division; with remainders");
		chooseOpBox.getChildren().addAll(chooseOp,opSelection);
		chooseOpBox.setSpacing(35);
		chooseOpButton = chooseOp;
		this.opSelection = opSelection;

		RadioButton randomOp = new RadioButton("Randomize operation");
		this.randomOpButton = randomOp;
		
		ToggleGroup opGroup = new ToggleGroup();
		opGroup.getToggles().addAll(chooseOp,randomOp);
		chooseOp.setSelected(true);
		
		VBox opBox = new VBox();
		opBox.getChildren().addAll(chooseOpBox,randomOp);
		opBox.setSpacing(5);
		
		Button confirmButton = new Button("Generate Worksheet");
		confirmButton.setPrefHeight(60);
		confirmButton.setPrefWidth(180);
		confirmButton.setFont(Font.font("Bahnschrift",FontWeight.BOLD,15));
		
		VBox contentsBox = new VBox();
		contentsBox.getChildren().addAll(titleQsPane,numDigitsBox,useNegatives,opBox,confirmButton);
		contentsBox.setSpacing(30);
		contentsBox.setAlignment(Pos.TOP_CENTER);
		contentsBox.setPadding(new Insets(30));
		
		box.getChildren().addAll(headerBox,hLine,contentsBox);
		box.setAlignment(Pos.TOP_CENTER);
		BorderPane rootPane = new BorderPane();
		rootPane.setTop(homeMenu);
		rootPane.setCenter(box);
		
		Scene createWS = new Scene(rootPane,550,600);
		worksheetSelections = createWS;
		stage.setScene(createWS);
		
		confirmButton.setOnAction(e -> {
			String title = this.titleField.getText();
			int numQs = -1;
			try {
				numQs = Integer.parseInt(numQsField.getText());
			}
			catch(Exception exception)
			{
				errors.add("Error: invalid input. Please enter number of questions.");
			}
			
			int digit1 = this.digits1.getValue();
			if(digit1==0)
				errors.add("Error: Please choose the size of the first number.");
			
			int digit2 = this.digits2.getValue();
			if(digit2==0)
				errors.add("Error: Please choose the size of the second number.");
			
			boolean isNegative = false;
			if(this.useNegatives.isSelected())
				isNegative = true;
			
			Worksheet w;
			if(randomOpButton.isSelected())
			{
				if(errors.size()==0)
				{
					w = new Worksheet(numQs,digit1,digit2,isNegative);
					
					if(title.length()>0)
						w.setTitle(title);
					
					this.w = w;
					
					if(w.getLength()<=10)
						viewCols = 1;
					showWS(w,viewCols);
					//System.out.println(w.printSheet());
				}
				else {
					showErrors();
				}

			}
			else if(chooseOpButton.isSelected())
			{
				int opIndex = -1;
				
				String op;
				try {
					op = opSelection.getValue();
					
					if(op.charAt(0)=='A')
						opIndex = 0;
					else if(op.charAt(0)=='S')
						opIndex = 1;
					else if(op.charAt(0)=='M')
						opIndex = 2;
					else if(op.contains("no"))
						opIndex = 3;
					else if(op.contains("with"))
						opIndex = 4;
					else errors.add("Error: invalid operation selection");
				}
				catch (Exception exception)
				{
					op = "";
					errors.add("Error: please choose an operation.");
				}
				
				if(opIndex==1&&!useNegatives.isSelected()&&digit1!=0&&digit2!=0&&digit2>digit1)
				{
					errors.add("Error: second number cannot be larger than first number if subtracting without negatives.");
				}
				if(opIndex==3&&digit1!=0&&digit2!=0&&digit2>digit1)
				{
					errors.add("Error: second number cannot be larger than first number if dividing without remainders.");
				}
				
				if(errors.size()==0)
				{
					w = new Worksheet(numQs,opIndex,digit1,digit2,isNegative);
					
					if(title.length()>0)
						w.setTitle(title);
					
					this.w = w;
					
					if(w.getLength()<=10)
						viewCols = 1;
					
					showWS(w,viewCols);
					//System.out.println(w.printSheet());
				}
				else {
					showErrors();
				}
			}
			
		});
		
		stage.show();
		
		//set up error popup
		errorPop = new Stage();
		errorBox = new VBox();
		
		errorScene = new Scene(errorBox,400,300);
		
		//create answer scene
		BorderPane wsRoot = new BorderPane();
		
		MenuBar wsMenu = new MenuBar();
		Menu wsOptions = new Menu("Options");
		MenuItem wsStartOver = new MenuItem("Start Over");
		MenuItem wsQuit = new MenuItem("Quit");
		wsOptions.getItems().addAll(wsStartOver,wsQuit);
		Menu wsView = new Menu("View");
		MenuItem ws1Col = new MenuItem("1 Column");
		MenuItem ws2Col = new MenuItem("2 Columns");
		MenuItem ws3Col = new MenuItem("3 Columns");
		Menu wsSave = new Menu("Save");
		Menu wsSaveDoc = new Menu("Export as text document");
			MenuItem wsSave1 = new MenuItem("Questions only");
			wsSave1.setOnAction(e ->{
				try {
					saveWS(w,viewCols);
				} catch (IOException e1) {
					System.out.println("error");
				}
			});
			MenuItem wsSave2 = new MenuItem("Answers only");
			wsSave2.setOnAction(e ->{
				try {
					saveKey(w,viewCols);
				}
				catch (IOException e1)
				{
					System.out.println("error");
				}
			});
			MenuItem wsSave3 = new MenuItem("Questions and answers");
			wsSave3.setOnAction(e ->{
				try {
					saveWSAndKey(w,viewCols);
				} catch (IOException e1) {
					System.out.println("error");
				}
			});
			wsSaveDoc.getItems().addAll(wsSave1,wsSave2,wsSave3);
		Menu wsSaveDocPlain = new Menu("Export as plain text");
			MenuItem wsDoc1 = new MenuItem("Questions only");
			wsDoc1.setOnAction(e -> {
			try {
				saveTxt(true,false);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}}
			);
			MenuItem wsDoc2 = new MenuItem("Answers only");
			wsDoc2.setOnAction(e ->{
				try {
					saveTxt(false,true);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});
			MenuItem wsDoc3 = new MenuItem("Questions and answers");
			wsDoc3.setOnAction(e ->{
				try {
					saveTxt(true,true);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});
			wsSaveDocPlain.getItems().addAll(wsDoc1,wsDoc2,wsDoc3);
		Menu wsCopyClip = new Menu("Copy to clipboard (plain text)");
			MenuItem wsCopy1 = new MenuItem("Questions only");
			wsCopy1.setOnAction(e ->{
				clipboard.putString(w.printSheet());
				Clipboard.getSystemClipboard().setContent(clipboard);

			});
			MenuItem wsCopy2 = new MenuItem("Answers only");
			wsCopy2.setOnAction(e ->{
				clipboard.putString(w.printKey());
				Clipboard.getSystemClipboard().setContent(clipboard);
			});
			MenuItem wsCopy3 = new MenuItem("Questions and answers");
			wsCopy3.setOnAction(e ->{
				clipboard.putString(w.printSheet()+"\n"+w.printKey());
				Clipboard.getSystemClipboard().setContent(clipboard);
			});
			wsCopyClip.getItems().addAll(wsCopy1,wsCopy2,wsCopy3);
		wsSave.getItems().addAll(wsSaveDoc,wsSaveDocPlain,wsCopyClip);
		wsView.getItems().addAll(ws1Col,ws2Col,ws3Col);
		wsMenu.getMenus().addAll(wsOptions,wsView,wsSave);
		wsStartOver.setOnAction(e->{
			resetScreen();
		});
		wsQuit.setOnAction(e -> {
			System.exit(0);
		});
		ws1Col.setOnAction(e ->{
			if(viewCols!=1)
				showWS(w,1);
			viewCols = 1;
		});
		ws2Col.setOnAction(e ->{
			if(viewCols!=2)
				showWS(w,2);
			viewCols = 2;
		});
		ws3Col.setOnAction(e ->{
			if(viewCols!=3)
				showWS(w,3);
			viewCols = 3;
		});
		
		wsRoot.setTop(wsMenu);
		
		Label wsHeader = new Label("Worksheet Generator");
		wsHeader.setFont(Font.font("Ink Free",FontWeight.BOLD, 48));
		
		Line wsLine = new Line(25,45,505,45);

		VBox wsHeaderBox = new VBox();
		wsHeaderBox.getChildren().addAll(wsHeader,wsLine,new Label(" "));
		wsHeaderBox.setAlignment(Pos.TOP_CENTER);
		wsHeaderBox.setSpacing(15);
		BorderPane wsRoot1 = new BorderPane();
		wsRoot.setCenter(wsRoot1);
		wsRoot1.setTop(wsHeaderBox);
		
		HBox wsButtonsBox = new HBox();
		Button wsBackButton = new Button("Home");
		wsBackButton.setPrefWidth(100);
		wsBackButton.setPrefHeight(45);
		wsBackButton.setFont(Font.font(14));
		
		Button wsViewKey = new Button("Answer Key");
		wsViewKey.setPrefWidth(120);
		wsViewKey.setPrefHeight(45);
		wsViewKey.setFont(Font.font(14));
		wsButtonsBox.getChildren().addAll(wsBackButton,wsViewKey);
		wsButtonsBox.setAlignment(Pos.CENTER);
		wsButtonsBox.setSpacing(250);
		wsButtonsBox.setPadding(new Insets(15));		
		
		ScrollPane wsPane = new ScrollPane();
		wsBox = new VBox();
		wsPane.setFitToWidth(true);
		wsPane.setContent(wsBox);
		//wsBox.setSpacing(20);
		wsBox.setPadding(new Insets(20));
		wsBox.setAlignment(Pos.TOP_CENTER);
		//wsBox.setPrefWidth(480);
		wsRoot1.setCenter(wsPane);
		wsRoot1.setBottom(wsButtonsBox);
		wsRoot1.setPadding(new Insets(20));
		
		wsScene = new Scene(wsRoot,550,600);
		wsBackButton.setOnAction(e ->{
			stage.setScene(worksheetSelections);
		});
		wsViewKey.setOnAction(e ->{
			showKey(w,viewCols);
		});
		//stage.setScene(wsScene);
		
		//create key scene
		BorderPane keyRoot = new BorderPane();
		
		MenuBar keyMenu = new MenuBar();
		Menu keyOptions = new Menu("Options");
		MenuItem keyStartOver = new MenuItem("Start Over");
		MenuItem keyQuit = new MenuItem("Quit");
		keyOptions.getItems().addAll(keyStartOver,keyQuit);
		Menu keyView = new Menu("View");
		MenuItem key1Col = new MenuItem("1 Column");
		MenuItem key2Col = new MenuItem("2 Columns");
		MenuItem key3Col = new MenuItem("3 Columns");
		Menu keySave = new Menu("Save");
		Menu keySaveDoc = new Menu("Export as text document");
			MenuItem keySave1 = new MenuItem("Questions only");
			keySave1.setOnAction(e ->{
				try {
					saveWS(w,viewCols);
				} catch (IOException e1) {
					System.out.println("error");
				}
			});
			MenuItem keySave2 = new MenuItem("Answers only");
			keySave2.setOnAction(e ->{
				try {
					saveKey(w,viewCols);
				}
				catch (IOException e1)
				{
					System.out.println("error");
				}
			});
			MenuItem keySave3 = new MenuItem("Questions and answers");
			keySave3.setOnAction(e ->{
				try {
					saveWSAndKey(w,viewCols);
				} catch (IOException e1) {
					System.out.println("error");
				}
			});
			keySaveDoc.getItems().addAll(keySave1,keySave2,keySave3);
		Menu keySaveDocPlain = new Menu("Export as plain text");
			MenuItem keyDoc1 = new MenuItem("Questions only");
			MenuItem keyDoc2 = new MenuItem("Answers only");
			MenuItem keyDoc3 = new MenuItem("Questions and answers");
			keySaveDocPlain.getItems().addAll(keyDoc1,keyDoc2,keyDoc3);
		Menu keyCopyClip = new Menu("Copy to clipboard (plain text)");
			MenuItem keyCopy1 = new MenuItem("Questions only");
			keyCopy1.setOnAction(e ->{
				clipboard.putString(w.printSheet());
				Clipboard.getSystemClipboard().setContent(clipboard);

			});
			MenuItem keyCopy2 = new MenuItem("Answers only");
			keyCopy2.setOnAction(e ->{
				clipboard.putString(w.printKey());
				Clipboard.getSystemClipboard().setContent(clipboard);
			});
			MenuItem keyCopy3 = new MenuItem("Questions and answers");
			keyCopy3.setOnAction(e ->{
				clipboard.putString(w.printSheet()+"\n"+w.printKey());
				Clipboard.getSystemClipboard().setContent(clipboard);
			});
			keyCopyClip.getItems().addAll(keyCopy1,keyCopy2,keyCopy3);
		keySave.getItems().addAll(keySaveDoc,keySaveDocPlain,keyCopyClip);
		keyView.getItems().addAll(key1Col,key2Col,key3Col);
		keyMenu.getMenus().addAll(keyOptions,keyView,keySave);
		keyStartOver.setOnAction(e->{
			resetScreen();
		});
		keyQuit.setOnAction(e -> {
			System.exit(0);
		});
		key1Col.setOnAction(e ->{
			if(viewCols!=1)
				showKey(w,1);
			viewCols = 1;
		});
		key2Col.setOnAction(e ->{
			if(viewCols!=2)
				showKey(w,2);
			viewCols = 2;
		});
		key3Col.setOnAction(e ->{
			if(viewCols!=3)
				showKey(w,3);
			viewCols = 3;
		});
		
		keyRoot.setTop(keyMenu);
		
		Label keyHeader = new Label("Worksheet Generator");
		keyHeader.setFont(Font.font("Ink Free",FontWeight.BOLD, 48));
		
		Line keyLine = new Line(25,45,505,45);

		VBox keyHeaderBox = new VBox();
		keyHeaderBox.getChildren().addAll(keyHeader,keyLine,new Label(" "));
		keyHeaderBox.setAlignment(Pos.TOP_CENTER);
		keyHeaderBox.setSpacing(15);
		BorderPane keyRoot1 = new BorderPane();
		keyRoot.setCenter(keyRoot1);
		keyRoot1.setTop(keyHeaderBox);
		
		HBox keyButtonsBox = new HBox();
		Button keyBackButton = new Button("Home");
		keyBackButton.setPrefWidth(100);
		keyBackButton.setPrefHeight(45);
		keyBackButton.setFont(Font.font(14));
		
		Button keyViewQs = new Button("Questions");
		keyViewQs.setPrefWidth(120);
		keyViewQs.setPrefHeight(45);
		keyViewQs.setFont(Font.font(14));
		keyButtonsBox.getChildren().addAll(keyBackButton,keyViewQs);
		keyButtonsBox.setAlignment(Pos.CENTER);
		keyButtonsBox.setSpacing(250);
		keyButtonsBox.setPadding(new Insets(15));		
		
		ScrollPane keyPane = new ScrollPane();
		keyBox = new VBox();
		keyPane.setContent(keyBox);
		//keyBox.setSpacing(20);
		keyBox.setPadding(new Insets(20));
		keyBox.setAlignment(Pos.TOP_CENTER);
		//keyBox.setPrefWidth(480);
		keyRoot1.setCenter(keyPane);
		keyRoot1.setBottom(keyButtonsBox);
		keyRoot1.setPadding(new Insets(20));
		
		keyScene = new Scene(keyRoot,550,600);
		keyBackButton.setOnAction(e ->{
			stage.setScene(worksheetSelections);
		});
		keyViewQs.setOnAction(e ->{
			showWS(w,viewCols);
		});
	}
	
	public void showErrors()
	{
		errorBox.getChildren().removeAll(errorBox.getChildren());
		
		Label l;
		for(String s:errors)
		{
			l = new Label(s);
			l.setWrapText(true);
			l.setFont(Font.font("Bahnschrift",18));
			errorBox.getChildren().add(l);
			errorBox.setSpacing(20);
			errorBox.setPadding(new Insets(15));
		}
				
		errors = new ArrayList<String>();
				
		errorPop.setScene(errorScene);
		errorPop.show();
	}
	
	public void showWS(Worksheet w,int cols)
	{
		Label l;
		wsBox.getChildren().removeAll(wsBox.getChildren());
		
		if(w.getTitle().length()>0)
		{
			l = new Label(w.getTitle());
			wsBox.getChildren().add(l);
			
		}
		
		HBox problemsBox = new HBox();
		VBox lastBox = new VBox();
		int colSize = w.getLength()/cols;
		int lastIndex = 0;
		
		int test = w.getLength();
		int count = 0;
		while(test>0)
		{
			test /=10;
			count++;
		}
		int occupiedWidth = 35+count*10+5+w.getScale()*10;
		for(int j = 0;j<cols;j++)
		{
			VBox colBox = new VBox();
			for(int i = j*colSize;i<(1+j)*colSize;i++)
			{
				l = new Label((i+1)+". "+w.getQuestion(i));
				colBox.getChildren().add(l);
				lastIndex = i;
			}
			lastBox = colBox;
			colBox.setSpacing(15);
			problemsBox.getChildren().addAll(colBox);
		
		}
		if(lastIndex<w.getLength()-1)
		{
			for(int i = lastIndex+1;i<w.getLength();i++)
			{
				l = new Label((i+1)+". "+w.getQuestion(i));
				lastBox.getChildren().add(l);
			}
		}
		
		if(cols>1&&occupiedWidth*cols<480)
		{
			problemsBox.setSpacing((450-occupiedWidth*cols)/(cols-1));
			System.out.println("WS: "+occupiedWidth+"\t"+(450-occupiedWidth*cols)/(cols-1));
		}
		else problemsBox.setSpacing(30);
		
		wsBox.getChildren().add(problemsBox);
		stage.setScene(wsScene);
	}
	
	public void showKey(Worksheet w,int cols)
	{
		Label l;
		keyBox.getChildren().removeAll(keyBox.getChildren());
		if(w.getTitle().length()>0)
		{
			l = new Label(w.getTitle()+": ANSWER KEY");
		}
		else l = new Label("ANSWER KEY");
		keyBox.getChildren().add(l);

		HBox answerBox = new HBox();
		VBox lastBox = new VBox();
		int colSize = w.getLength()/cols;
		int lastIndex = 0;
		
		int test = w.getLength();
		int count = 0;
		while(test>0)
		{
			test /=10;
			count++;
		}
		int occupiedWidth = 5+count*10+5+w.getLongestAns()*10;		
		
		for(int j = 0;j<cols;j++)
		{
			VBox colBox = new VBox();
			for(int i = j*colSize;i<(1+j)*colSize;i++)
			{
				l = new Label((i+1)+". "+w.getAnswer(i));
				colBox.getChildren().add(l);
				lastIndex = i;
			}
			lastBox = colBox;
			colBox.setSpacing(15);
			answerBox.getChildren().addAll(colBox);
		
		}
		if(lastIndex<w.getLength()-1)
		{
			for(int i = lastIndex+1;i<w.getLength();i++)
			{
				l = new Label((i+1)+". "+w.getAnswer(i));
				lastBox.getChildren().add(l);
			}
		}
		if(cols>1&&occupiedWidth*cols<480)
		{
			answerBox.setSpacing((450-occupiedWidth*cols)/(cols-1));
			System.out.println("key: "+occupiedWidth+"\t"+(450-occupiedWidth*cols)/(cols-1));
		}

		
		keyBox.getChildren().add(answerBox);
		stage.setScene(keyScene);
	}
	
	public void saveWS(Worksheet w, int cols) throws IOException
	{
		XWPFDocument document;
		if(cols ==1)
			document = new XWPFDocument(MainTest.class.getResourceAsStream("/t/oneColumn.docx"));
		else if(cols ==2)
			document = new XWPFDocument(MainTest.class.getResourceAsStream("/t/twoColumns.docx"));
		else if(cols == 3)
			document = new XWPFDocument(MainTest.class.getResourceAsStream("/t/threeColumns.docx"));
		else return;
		
	    XWPFParagraph tmpParagraph1 = document.getParagraphs().get(0);
	    	    
	    XWPFRun tmpRun1 = tmpParagraph1.createRun();
	    tmpRun1.setText(w.getTitle());
	    
	    XWPFParagraph tmpParagraph2 = document.getParagraphs().get(1);
	    XWPFRun run1 = tmpParagraph2.createRun();
	    run1.setText("1. "+w.getQuestion(0));
	    
	    for (int i = 1; i < w.getLength(); i++) {
	    	XWPFParagraph par = document.createParagraph();
	        XWPFRun tmpRun = par.createRun();
	        tmpRun.setText((i+1)+". "+w.getQuestion(i));
	        
	    }
	    
	    FileChooser chooser = new FileChooser();
		chooser.getExtensionFilters().add(new ExtensionFilter("Word Docs", "*.docx"));
		
	    File file = chooser.showSaveDialog(stage);
	    
	    document.write(new FileOutputStream(file));
	    
	    document.close();
	}
	
	public void saveKey(Worksheet w, int cols) throws IOException
	{
		XWPFDocument document;
		if(cols ==1)
			document = new XWPFDocument(MainTest.class.getResourceAsStream("/t/oneColumn.docx"));
		else if(cols ==2)
			document = new XWPFDocument(MainTest.class.getResourceAsStream("/t/twoColumns.docx"));
		else if(cols == 3)
			document = new XWPFDocument(MainTest.class.getResourceAsStream("/t/threeColumns.docx"));
		else return;
		
	    XWPFParagraph tmpParagraph1 = document.getParagraphs().get(0);
	    	    
	    XWPFRun tmpRun1 = tmpParagraph1.createRun();
	    if(w.getTitle().length()>0)
	    	tmpRun1.setText(w.getTitle()+": ANSWER KEY");
	    else tmpRun1.setText("ANSWER KEY");
	    
	    XWPFParagraph tmpParagraph2 = document.getParagraphs().get(1);
	    XWPFRun run1 = tmpParagraph2.createRun();
	    run1.setText("1. "+w.getAnswer(0));
	    
	    for (int i = 1; i < w.getLength(); i++) {
	    	XWPFParagraph par = document.createParagraph();
	        XWPFRun tmpRun = par.createRun();
	        tmpRun.setText((i+1)+". "+w.getAnswer(i));
	        
	    }
	    
	    FileChooser chooser = new FileChooser();
		chooser.getExtensionFilters().add(new ExtensionFilter("Word Docs", "*.docx"));
		
	    File file = chooser.showSaveDialog(stage);
	    
	    document.write(new FileOutputStream(file));
	    
	    document.close();
	}
	
	public void saveWSAndKey(Worksheet w, int cols) throws IOException
	{
		XWPFDocument document;
		if(cols ==1)
			document = new XWPFDocument(MainTest.class.getResourceAsStream("/t/oneColumn2.docx"));
		else if(cols ==2)
			document = new XWPFDocument(MainTest.class.getResourceAsStream("/t/twoColumns2.docx"));
		else if(cols == 3)
			document = new XWPFDocument(MainTest.class.getResourceAsStream("/t/threeColumns2.docx"));
		else return;
		
	    XWPFParagraph par1 = document.getParagraphs().get(0);
	    XWPFRun run1 = par1.createRun();
	    run1.setText(w.getTitle());
	    
	    XWPFParagraph par2 = document.getParagraphs().get(1);
	    XWPFRun run2 = par2.createRun();
	    run2.setText("1. "+w.getQuestion(0));
	    run2.addBreak();
	    
	    for (int i = 1; i < w.getLength(); i++) {
	        XWPFRun tmpRun = par2.createRun();
	        tmpRun.setText((i+1)+". "+w.getQuestion(i));
	        tmpRun.addBreak();
	        
	    }
	    par2.setSpacingBetween(2);
	    
	    XWPFParagraph par3 = document.getParagraphs().get(4);
	    XWPFRun run3 = par3.createRun();
	    if(w.getTitle().length()>0)
	    	run3.setText(w.getTitle()+": ANSWER KEY");
	    else run3.setText("ANSWER KEY");
	    
	    XWPFParagraph par4 = document.getParagraphs().get(5);
	    XWPFRun run4 = par4.createRun();
	    run4.setText("1. "+w.getAnswer(0));
	    run4.addBreak();
	    
	    for (int i = 1; i < w.getLength(); i++) {
	    	 XWPFRun tmpRun = par4.createRun();
		     tmpRun.setText((i+1)+". "+w.getAnswer(i));
		     tmpRun.addBreak();
		    
	    }
	    par4.setSpacingBetween(2);
	    
	    
	    FileChooser chooser = new FileChooser();
		chooser.getExtensionFilters().add(new ExtensionFilter("Word Docs", "*.docx"));
		
	    File file = chooser.showSaveDialog(stage);
	    
	    document.write(new FileOutputStream(file));
	    
	    document.close();
	}
	
	public void saveTxt(boolean ws, boolean key) throws IOException
	{
		
		FileChooser chooser = new FileChooser();
		chooser.getExtensionFilters().add(new ExtensionFilter("Text Files", "*.txt"));
		
		File file = chooser.showSaveDialog(stage);
		PrintWriter out = new PrintWriter((new FileWriter(file)));

		if(ws&&!key)			
			out.println(w.printSheet());
		else if(!ws&&key)
			out.println(w.printKey());
		else if(ws&&key)
			out.println(w.printSheet()+w.printKey());
		
		out.close();
	}
	
	public void resetScreen()
	{
		errors = new ArrayList<String>();
		
		titleField.setText("");
		numQsField.setText("");
				
		digits1.setValue(0);	
		digits2.setValue(0);

		useNegatives.setSelected(false);
		
		chooseOpButton.setSelected(true);
		randomOpButton.setSelected(false);
		
		opSelection.setValue("");
	
		stage.setScene(worksheetSelections);
	}
	
	public static void main(String[] args)
	{
		launch(args);
	}

}
