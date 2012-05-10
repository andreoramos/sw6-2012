package dk.aau.cs.giraf.parrot;

import java.util.ArrayList;

/**
 * 
 * @Rasmus
 * This is the PARROT Profile class. It handles the information associated with the current user, such as available pictograms
 * organized in categories and settings, as well as personal information whenever it is needed.
 */
public class PARROTProfile {
	private String name;
	private Pictogram icon;
	private ArrayList<Category> categories = new ArrayList<Category>();
	private long profileID;
	private int NumberOfSentencePictograms = 4;
	private boolean[] rights = new boolean[3];
	private int categoryColor = 0;
	private int sentenceBoardColor = 0;
	
	//TODO add settings such as colour specification and others...
	
	public PARROTProfile(String name, Pictogram icon)
	{
		this.setName(name);
		this.setIcon(icon);
	}

	public ArrayList<Category> getCategories() {
		return categories;
	}
	
	
	public Category getCategoryAt(int i)
	{
		return categories.get(i);
	}
	
	public void addCategory(Category cat)
	{
		categories.add(cat);
	}
	
	public void removeCaregory(int i)
	{
		categories.remove(i);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Pictogram getIcon() {
		return icon;
	}

	public void setIcon(Pictogram icon) {
		this.icon = icon;
	}

	public long getProfileID() {
		return profileID;
	}

	public void setProfileID(long l) {
		this.profileID = l;
	}

	public int getNumberOfSentencePictograms() {
		return NumberOfSentencePictograms;
	}

	public void setNumberOfSentencePictograms(int numberOfSentencePictograms) {
		NumberOfSentencePictograms = numberOfSentencePictograms;
	}
	
	/*
	 * setRights is save what functionalities a person is allowed to access.
	 * Expected to be called as an users profile is being established or edited.
	 */
	public void setRights(int i, boolean yesOrNo)
	{
		this.rights[i] = yesOrNo;
	}
	
	/* 
	 * The getRights getMethod is used in Parrot Activity to determine if a given user is allowed 
	 * to use certain functionalities in the App.
	 * i is to determine which functionality is in question.
	 * Remember: Make sure the order of the Tabs is consistent with the order of their rights in the
	 * 			 Rights array.
	 * Read: Consider reading comment on tab initialization in PARROTActivity.Java
	 */
	public boolean getRights(int i)
	{
		boolean isAllowed = this.rights[i];
		return isAllowed;
	}

	public int getCategoryColor() {
		return categoryColor;
	}

	public void setCategoryColor(int categoryColor) {
		this.categoryColor = categoryColor;
	}

	public int getSentenceBoardColor() {
		return sentenceBoardColor;
	}

	public void setSentenceBoardColor(int sentenceBoardColor) {
		this.sentenceBoardColor = sentenceBoardColor;
	}
	
}
