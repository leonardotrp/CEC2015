package br.ufrj.coc.cec2014.functions;
import java.io.File;
import java.net.URL;
import java.util.Locale;
import java.util.Scanner;

public class testmain {
	public static void main(String[] args) throws Exception{
		double[] OShift,M,y,z,x_bound;
		int ini_flag = 0,n_flag,func_flag;
		int[] SS;
		
		int i,j,k,n,m,func_num;
		double[] f,x;
		File fpt;
		
		m=3;
		n=10;
		
		x = new double[m*n];
		f = new double[m];
		
		testfunc tf = new testfunc();
		
		for (i=0;i<30;i++)
		{
			func_num = i+1;
			// @leonardo
			URL urlInput = testfunc.getInputData("shift_data_" + func_num + ".txt");
			fpt = new File(urlInput.toURI());
			Scanner input = new Scanner(fpt);
			input.useLocale(Locale.US);
			
			for(k=0;k<n;k++)
			{
				x[k] = input.nextDouble();
			}
			
			for(int ii=0;ii<n;ii++){
				//System.out.println(x[i]);
			}
			
			input.close();
			
			
			for(j=0;j<n;j++)
			{
				x[1*n+j] = 0.0;
				//System.out.println(x[1*n+j]);
			}
			for (j = 0; j < n; j++)
			{
				x[2*n+j]=10.0;
				/*printf("%Lf\n",x[1*n+j]);*/
			}
			
			//fpt = new File("randdata_"+func_num+".txt");
			// input = new Scanner(fpt);
			
			//for(j=0;j<n;j++)
			//{
			//	x[3*n+j] = input.nextDouble();
			//}
			
			//input.close();
			
			

			
			for (k=0;k<1;k++)
			{
				tf.test_func(x,f,n,m,func_num);
				for(j=0;j<m;j++)
				{
					System.out.println("f"+func_num+"(x["+(j+1)+"])="+f[j]);
				}
			}
			
			
			
		}
		
		
	}
}
