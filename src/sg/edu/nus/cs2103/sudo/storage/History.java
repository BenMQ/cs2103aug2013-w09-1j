package sg.edu.nus.cs2103.sudo.storage;

import java.io.Serializable;
import java.util.ArrayList;

public class History implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * This History class is to handle with history time line("undo").
	 * 
	 * @author Liu Dake
	 */
	private ArrayList<HistorySlice> historyList;
	private String historyName;
	
	public History(String name){
		historyName = name;
		historyList = new ArrayList<HistorySlice>();
	}
	
	public void add(HistorySlice slice){
		historyList.add(slice);
	}
	public HistorySlice undo(){
		historyList.remove(historyList.size()-1);
		return historyList.get(historyList.size()-1);
		}
	public void addNull(){
		//Null history slice represents the first slice, i.e. user can't undo when the bottom is reached.
		HistorySlice nullSlice = new HistorySlice(true);
		historyList.add(nullSlice);
	}
	public Boolean undoable(){
		return historyList.size()>1;
	}
	public String getName(){
		return historyName;
	}
	
}

