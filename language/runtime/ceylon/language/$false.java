package ceylon.language;

public class $false {
    private final static Boolean value = new Boolean(String.instance("false")){
			@Override
			public boolean booleanValue() {
				return false;
			}
		};
    
    public static Boolean getFalse(){
        return value;
    }
}
