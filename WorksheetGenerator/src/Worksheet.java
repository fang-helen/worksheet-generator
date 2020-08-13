import java.util.ArrayList;

public class Worksheet 
{
	int numQs;
	static String[] operations = {"+","-","x","÷","÷"};
	int operation;
	
	int digits1;
	int digits2;
	boolean negative;
	
	String text;
	ArrayList<String> questions;
	ArrayList<String> answerKey;
	
	String title;
	
	public Worksheet(int numQs,int operation, int digits1,int digits2,boolean negative)
	{
		this.numQs = numQs;
		this.digits1 = digits1;
		this.digits2 = digits2;
		this.operation = operation;
		this.negative = negative;
		
		text = "";
		title = "";
		
		questions = new ArrayList<String>();
		answerKey = new ArrayList<String>();
		
		for(int i = 0;i<numQs;i++)
		{
			generateQuestion(digits1,digits2,operation, negative);
			
		}
	
		
	}
	
	public Worksheet(int numQs,int digits1,int digits2,boolean negative)
	{
		this.numQs = numQs;
		this.digits1 = digits1;
		this.digits2 = digits2;
		this.negative = negative;
		
		text = "";
		title = "";
		
		questions = new ArrayList<String>();
		answerKey = new ArrayList<String>();
		
		int operation;
		for(int i = 0;i<numQs;i++)
		{
			operation = (int)(Math.random()*5);
			generateQuestion(digits1,digits2,operation,negative);	
			
		}
	}
	
	public void generateQuestion(int count1,int count2,int operation, boolean negative)
	{
		int num1 = 0;
		int num2 = 0;
		
		int digit;
		
		while(count1>=0)
		{
			digit = (int)(Math.random()*10);
			num1+=digit*Math.pow(10, count1-1);
			count1--;
		}
		
		while(count2>=0)
		{
			digit = (int)(Math.random()*10);
			num2+=digit*Math.pow(10, count2-1);
			count2--;
		}

		if(operation == 1)
		{
			if(!negative)
			{
				while(num2>num1)
				{
					num2 = 0;
					num1 = 0;
					count1 = digits1;
					count2 = digits2;
					while(count1>=0)
					{
						digit = (int)(Math.random()*10);
						num1+=digit*Math.pow(10, count1-1);
						count1--;
					}
					
					while(count2>=0)
					{
						digit = (int)(Math.random()*10);
						num2+=digit*Math.pow(10, count2-1);
						count2--;
					}
				}
			}
		}
		
		if (operation == 3||operation == 4)
		{
			while(num2==0||num2>num1)
			{
				if(num2==0)
				{
					count2 = digits2;
					
					while(count2>=0)
					{
						digit = (int)(Math.random()*10);
						num2+=digit*Math.pow(10, count2-1);
						count2--;
					}
				}
				if(num2>num1)
				{
					num2 = 0;
					num1 = 0;
					count1 = digits1;
					count2 = digits2;
					while(count1>=0)
					{
						digit = (int)(Math.random()*10);
						num1+=digit*Math.pow(10, count1-1);
						count1--;
					}
					
					while(count2>=0)
					{
						digit = (int)(Math.random()*10);
						num2+=digit*Math.pow(10, count2-1);
						count2--;
					}
				}
			}
									
			if(operation ==3 && num1%num2!=0)
			{
				int temp = num1/num2;
				num1 = num2*temp;
			}
		}
		
		int test;
		if(negative&&operation!=4)
		{
			test = (int)(Math.random()*10);
			if(test<=4)
				num1*=-1;
			test = (int)(Math.random()*10);
			if(test<=4)
				num2*=-1;
		}
		
		if(operation==1&&num2<0)
			questions.add(num1+" "+operations[operation]+" ("+num2+") =");
		else
			questions.add(num1+" "+operations[operation]+" "+num2+" =");
		
		if(operation ==0)
			answerKey.add(""+(num1+num2));
		else if(operation ==1)
			answerKey.add(""+(num1-num2));
		else if(operation ==2)
			answerKey.add(""+(num1*num2));
		else if (operation == 3)
			answerKey.add(""+(num1/num2));
		else if (operation ==4)
		{	
			if(num1%num2!=0)
				answerKey.add((num1/num2)+" R "+num1%num2);
			else answerKey.add(""+(num1/num2));

		}
		else System.out.println("invalid operation");
			
	}
	
	public void setTitle(String t)
	{
		title = t;
	}
	
	public String getQuestion(int i)
	{
		return questions.get(i);
	}
	
	public String getAnswer(int i)
	{
		return answerKey.get(i);
	}
	
	public int getLongestAns()
	{
		int max = 0;
		for(String s:answerKey)
			if(s.length()>max)
				max = s.length();
		return max;
	}
	
	public int getLongestQ()
	{
		int max = 0;
		//String l = questions.get(0);
		for(String s:questions)
			if(s.length()>max)
			{
				max = s.length();
				//l = s;
			}
		
		//System.out.println(l);
		return max;
	}
	
	public int getScale()
	{
		return digits1+digits2;
	}
	
	public int getLength()
	{
		return numQs;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public void printSheet(int cols) 
	{
		//tab width = 60 pixels
	}
	public void printKey(int cols)
	{
		
	}
	
	public String printKey()
	{
		String text = "";
		
		if(title.length()>0)
			text = title+": ";
		text += "ANSWER KEY"+"\n";
		//text += "\n";
		
		for(int i = 0;i<numQs;i++)
		{
			text += (i+1)+". "+answerKey.get(i);
			//text += "\n";
			text += "\n";
		}
		return text;
	}
	
	public String printSheet()
	{
		if(title.length()>0)
			text = title + "\n";
		
		for(int i = 0;i<questions.size();i++)
		{
			text += (i+1)+". ";
			text += questions.get(i);
			text += "\n";
		}

		return text;
	}
	
	public int operationIndex(String s)
	{
		if(s.equalsIgnoreCase("add")||s=="+")
			return 0;
		if(s.equalsIgnoreCase("subtract")||s=="-")
			return 1;
		if(s.equalsIgnoreCase("multiply")||s=="*"||s=="x")
			return 2;
		if(s.equalsIgnoreCase("divide")||s=="/"||s=="÷")
			return 3;
		
		return -1;
	}

}
