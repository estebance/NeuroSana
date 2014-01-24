package com.utilities.neurosana;

public class DecodeBi 
{
	String channels[] = {"F3","FC5","AF3","F7","T7","P7","O1","O2","P8","T8","F8","AF4","FC6","F4"};
	


	public DecodeBi()
	{
		
	}
	
	
	public String decode(int value)
	{
		 StringBuilder sb = new StringBuilder(""); 
		 String numerobinario  = Integer.toBinaryString(value);    
		 System.out.println("numero normal_:"+value);
		 System.out.println("numero en binario_: "+ numerobinario);
		 
		    for (int i = 1 ; i < 15 ; i++)
		    {
		    
		    String binario = ""+numerobinario.charAt(i);		    
		    
		    int bin = Integer.parseInt(binario);
		    
		    if (bin == 0 )
		    {  
		    System.out.println("Tenemos un 0 en_:" +channels[i-1]); 
		    
		    if(i == 1)
		    {
		     sb.append(channels[i-1]);   
		    }
		    else
		    {
		    sb.append("-"+channels[i-1]);    
		    }

		    }
		    
		    }
		    
		    return sb.toString();
	}
	
	

}
