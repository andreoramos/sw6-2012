package dk.aau.cs.giraf.parrot;

import android.app.Activity;
import android.view.DragEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.widget.GridView;
import android.widget.ListView;

public class ManagementBoxDragListener implements OnDragListener
{

	private Activity parrent;
	private Pictogram draggedPictogram = null;
	ListView categories;
	GridView pictograms;
	boolean insideOfMe = false;
	
	public ManagementBoxDragListener(Activity active) {
		parrent = active;
		categories = (ListView) parrent.findViewById(R.id.categories);
		pictograms = (GridView) parrent.findViewById(R.id.pictograms);
	}
	
	public boolean onDrag(View self, DragEvent event) {
		if (event.getAction() == DragEvent.ACTION_DRAG_STARTED){
			
			
			/*//From BoxDragListener. Not used here
			if(self.getId() == R.id.sentenceboard && SpeechBoardFragment.dragOwnerID == R.id.sentenceboard)
			{
				draggedPictogram = SpeechBoardFragment.speechBoardCategory.getPictogramAtIndex(SpeechBoardFragment.draggedPictogramIndex);
				if(draggedPictogram.isEmpty()==true)
				{
					//Do not allow dragging empty pictograms
				}
				else
				{
					GridView speech = (GridView) parrent.findViewById(R.id.sentenceboard);
					SpeechBoardFragment.speechBoardCategory.removePictogram(SpeechBoardFragment.draggedPictogramIndex);	
					SpeechBoardFragment.speechBoardCategory.addPictogram(new Pictogram("#usynlig#", null, null, null, parrent));
					speech.setAdapter(new PictogramAdapter(SpeechBoardFragment.speechBoardCategory, parrent));
				}
			}
			*/
			//Dummy
		} else if (event.getAction() == DragEvent.ACTION_DRAG_ENTERED){ 
			insideOfMe = true;
		} else if (event.getAction() == DragEvent.ACTION_DRAG_EXITED){
			insideOfMe = false;
		} else if (event.getAction() == DragEvent.ACTION_DROP){
			if (insideOfMe){

				if(self.getId()==R.id.trash && ManageCategoryFragment.catDragOwnerID == R.id.pictograms) //We are to delete a pictogram from a category
				{
					Category temp = ManageCategoryFragment.profileBeingModified.getCategoryAt(ManageCategoryFragment.currentCategoryId);
					temp.removePictogram(ManageCategoryFragment.draggedItemIndex);
					ManageCategoryFragment.profileBeingModified.setCategoryAt(ManageCategoryFragment.currentCategoryId, temp);
					
					pictograms.setAdapter(new PictogramAdapter(ManageCategoryFragment.profileBeingModified.getCategoryAt(ManageCategoryFragment.currentCategoryId), parrent));
				}
				
				else if(self.getId()==R.id.categories && ManageCategoryFragment.catDragOwnerID == R.id.pictograms) //We are to copy a pictogram into another category
				{
					
					draggedPictogram = ManageCategoryFragment.profileBeingModified.getCategoryAt(ManageCategoryFragment.currentCategoryId).getPictogramAtIndex(ManageCategoryFragment.draggedItemIndex); 
							
					ListView categories = (ListView) parrent.findViewById(R.id.categories);
					int x = (int)event.getX();
					int y = (int)event.getY();
					int index = categories.pointToPosition(x, y);
					
					Category temp = ManageCategoryFragment.profileBeingModified.getCategoryAt(index);
					temp.addPictogram(draggedPictogram);
					ManageCategoryFragment.profileBeingModified.setCategoryAt(ManageCategoryFragment.currentCategoryId, temp);
										
				}
				else if(self.getId()==R.id.pictograms && ManageCategoryFragment.catDragOwnerID == R.id.categories) //We are to copy a category into another category
				{	
					ListView categories = (ListView) parrent.findViewById(R.id.categories);
					
					Category categoryCopiedFrom = ManageCategoryFragment.profileBeingModified.getCategoryAt(ManageCategoryFragment.draggedItemIndex);
					
					Category temp;
					
					for(int i = 0; i < categoryCopiedFrom.getPictograms().size(); i++)
					{
						temp = ManageCategoryFragment.profileBeingModified.getCategoryAt(ManageCategoryFragment.currentCategoryId);
						temp.addPictogram(categoryCopiedFrom.getPictogramAtIndex(i)); 
						ManageCategoryFragment.profileBeingModified.setCategoryAt(ManageCategoryFragment.currentCategoryId, temp);
					}
					
					pictograms.setAdapter(new PictogramAdapter(ManageCategoryFragment.profileBeingModified.getCategoryAt(ManageCategoryFragment.currentCategoryId), parrent));
				}
				else if(self.getId()==R.id.trash && ManageCategoryFragment.catDragOwnerID == R.id.categories) //We are to delete a category
				{	
					ManageCategoryFragment.profileBeingModified.removeCategory(ManageCategoryFragment.draggedItemIndex);
					categories.setAdapter(new ListViewAdapter(parrent, R.layout.categoriesitem, ManageCategoryFragment.profileBeingModified.getCategories()));
				}
				else
				{
					//TODO check that nothing is done. 
				}
			}
			//To ensure that no wrong references will be made, the index is reset to -1
			ManageCategoryFragment.draggedItemIndex = -1;
		} else if (event.getAction() == DragEvent.ACTION_DRAG_ENDED){
			//Dummy				
		}
		return true;
	}
}


