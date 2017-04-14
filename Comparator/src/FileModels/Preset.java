package FileModels;

public class Preset {
	private String preset;
	private String presetName;
	private String presetType;
	private String presetValue;
	
	public Preset(String preset){
		this.preset = preset;
		presetName = preset.split(":")[0];
		presetType = preset.split(":")[preset.split(":").length - 1].split("=")[0];
		presetValue = preset.split("=")[ preset.split("=").length - 1];
	}
	
	public String getPreset() {
		return preset;
	}
	
	public boolean equals(Preset toCompare){
		if(this.toString().equalsIgnoreCase(toCompare.toString())){
			return true;
		}		
		return false;
	}
	
	public String getPresetName() {
		return presetName;
	}

	public String getPresetType() {
		return presetType;
	}

	public String getPresetValue() {
		return presetValue;
	}
	
	public String toString(){
		return preset;
	}
	
	
}
