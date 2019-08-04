package Project;

public class Rule {
	
	public enum Result {OK, NOT_OK,ERROR}
	private String source;
	private String destination;
	private int port;
	private String allow;
	private Result result;
	

	public Rule(String source,String destination,int port, String allow) {
		
		this.source = source;
		this.destination = destination;
		this.port = port;
		this.allow = allow;
	}
	
	public Rule() {
		
	}


	public String getSource() {
		return source;
	}


	public void setSource(String source) {
		this.source = source;
	}


	public String getDestination() {
		return destination;
	}


	public void setDestination(String destination) {
		this.destination = destination;
	}


	public int getPort() {
		return port;
	}


	public void setPort(int port) {
		this.port = port;
	}
	
	
	public String getAllow() {
		return allow;
	}
	
	
	public void setAllow(String allow) {
		this.allow = allow;
	}
	
	public Result getResult() {
		return result;
	}
	
	
	public void setResult(Result result) {
		this.result = result;
	}

	
	@Override
	public String toString() {
		return "Rule: [source=" + source + ", destination=" + destination + ", port=" + port +", allow="+allow + ", result = " + result+"]";
	}
	
}
