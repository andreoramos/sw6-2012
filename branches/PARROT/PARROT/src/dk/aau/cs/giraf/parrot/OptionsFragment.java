package dk.aau.cs.giraf.parrot;




import yuku.ambilwarna.AmbilWarnaDialog;
import yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;



public class OptionsFragment extends Fragment
{
	private CheckBox checkboxGradient;
	
	private Activity parrent;
	@Override
	public void onAttach(Activity activity) 
	{
		super.onAttach(activity);
		this.parrent = activity;
		
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initColorButtons();
	}
	private void initColorButtons() {
		
		Button ccc = (Button) parrent.findViewById(R.id.changecategorycolor);// these are two different buttons
		
		Button csc = (Button) parrent.findViewById(R.id.changesentencecolor);
	
		
		ccc.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				AmbilWarnaDialog dialog = new AmbilWarnaDialog(getActivity(),
						PARROTActivity.getUser().getCategoryColor(), new OnAmbilWarnaListener() {
							public void onCancel(AmbilWarnaDialog dialog) {
							}

							public void onOk(AmbilWarnaDialog dialog, int color) {
								PARROTProfile user = PARROTActivity.getUser();
								user.setCategoryColor(color);
								PARROTActivity.setUser(user);
								SpeechBoardFragment.setColours(parrent);
							
							}
						});
				dialog.show();
			}
		});
		
		csc.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				AmbilWarnaDialog dialog = new AmbilWarnaDialog(getActivity(),
						PARROTActivity.getUser().getSentenceBoardColor(), new OnAmbilWarnaListener() {
							public void onCancel(AmbilWarnaDialog dialog) {
							}

							public void onOk(AmbilWarnaDialog dialog, int color) {
								PARROTProfile user = PARROTActivity.getUser();
								user.setSentenceBoardColor(color);
								PARROTActivity.setUser(user);
								SpeechBoardFragment.setColours(parrent);
							
							}
						});
				dialog.show();
				
			}
		});
	}
}
