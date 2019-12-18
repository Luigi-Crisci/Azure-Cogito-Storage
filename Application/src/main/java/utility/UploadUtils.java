package utility;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import javax.servlet.http.Part;

public class UploadUtils {
	
	 public static final String imageEndpoint ="vision/v2.1/analyze";

	 //Blank file used to create directory
	 public static Part blankFile = new Part() {
		
		@Override
		public void write(String fileName) throws IOException {
			
		}
		
		@Override
		public String getSubmittedFileName() {
			return ".blank";
		}
		
		@Override
		public long getSize() {
			return 1;
		}
		
		@Override
		public String getName() {
			return ".blank";
		}
		
		@Override
		public InputStream getInputStream() throws IOException {
			return new InputStream() {
				int i=0;
				@Override
				public int read() throws IOException {
					if(i==1) {
						return -1;
					}else {
						i++;
						return 1;
					}
				}
			};
		}
		
		@Override
		public Collection<String> getHeaders(String name) {
			return null;
		}
		
		@Override
		public Collection<String> getHeaderNames() {
			return null;
		}
		
		@Override
		public String getHeader(String name) {
			return null;
		}
		
		@Override
		public String getContentType() {
			return null;
		}
		
		@Override
		public void delete() throws IOException {			
		}
	};
}
