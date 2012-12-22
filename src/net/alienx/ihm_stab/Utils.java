package net.alienx.ihm_stab;

public class Utils {
	public static double map(double value,double or_min,double or_max, double dst_MIN, double dst_MAX)
	{
		return (value-or_min)*(dst_MAX-dst_MIN)/(or_max-or_min)+dst_MIN;
	}
	
	public static int toInt(byte[] data,int index){
		int tmp;
		index = index*2;
		tmp = (data[index] << 8) & 0xFF00;
		tmp += (data[index+1] & 0xFF);
		return tmp;
	}
	
	public static void toHex(byte[] data, int len){
		for(int i=0;i<len;i++){
			System.out.print(Integer.toHexString(data[i])+" ");
		}
		System.out.println();	
	}
	
	public static int constrain(int value,int min, int max){
		if(value < min)
			return min;
		if(value > max)
			return max;
		
		return value;
	}
	
	public static double constrain(double value,double min, double max){
		if(value < min)
			return min;
		if(value > max)
			return max;
		
		return value;
	}
}
