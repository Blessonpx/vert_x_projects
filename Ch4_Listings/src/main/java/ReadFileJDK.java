import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ReadFileJDK {
	public static void main(String[] args) {
		File file =new  File("/home/blesson/code/java_files/vert_x_projects/Ch4_Listings/Abc.txt");
		byte[] buffer = new byte[1024];
		try(FileInputStream in = new FileInputStream(file)) {
			int count = in.read(buffer);
			while(count !=-1) {
				System.out.println(new String(buffer,0,count));
				count=in.read(buffer);
			}
		}catch (IOException e){
			e.printStackTrace();
		}finally {
			System.out.println("\n------Done");
		}
	}
}
