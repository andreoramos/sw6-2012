package dk.aau.cs.giraf.wombat;

import java.util.ArrayList;
import java.util.List;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.NumericWheelAdapter;
import yuku.ambilwarna.AmbilWarnaDialog;
import yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import dk.aau.cs.giraf.TimerLib.Child;
import dk.aau.cs.giraf.TimerLib.Guardian;
import dk.aau.cs.giraf.TimerLib.SubProfile;
import dk.aau.cs.giraf.TimerLib.formFactor;

public class CustomizeFragment extends Fragment {
	private SubProfile preSubP;
	private SubProfile currSubP;
	private Guardian guard = Guardian.getInstance();

	private Button hourglassButton;
	private Button timetimerButton;
	private Button progressbarButton;
	private Button digitalButton;
	private Button startButton;
	private Button saveButton;
	private Button saveAsButton;
	private Button attachmentButton;
	private Button colorGradientButton1;
	private Button colorGradientButton2;
	private Button colorFrameButton;
	private Button colorBackgroundButton;

	private CheckBox checkboxGradient;

	private WheelView mins;
	private WheelView secs;

	private TextView timeDescription;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Write Tag = Detail and Text = Detail Opened in the LogCat
		Log.e("Customize", "Customize Opened");

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Populate the fragment according to the details layout
		return inflater.inflate(R.layout.customize, container, false);
	}

	@Override
	// Start the list empty
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Button b = (Button) getActivity()
				.findViewById(R.id.new_template_button);
		b.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				TimerLoader.subProfileID = -1;
				SubProfileFragment spf = (SubProfileFragment) getFragmentManager()
						.findFragmentById(R.id.subprofileFragment);
				spf.reloadSubProfiles();
				CustomizeFragment cf = (CustomizeFragment) getFragmentManager()
						.findFragmentById(R.id.customizeFragment);
				cf.setDefaultProfile();
				Toast t = Toast.makeText(getActivity(),
						getString(R.string.new_template_button_text),
						Toast.LENGTH_SHORT);
				t.show();
			}
		});

		currSubP = new SubProfile("", "", 10, 0xFF000000, 0xFFFF0000,
				0xFFFFFFFF, 0xFFFF0000, 600, false);
		currSubP.save = false;
		currSubP.saveAs = false;

		/********* TIME CHOSER *********/
		initStyleChoser();

		/********* TIMEPICKER *********/
		initTimePicker();

		/********* COLORPICKER *********/
		initColorButtons();

		/********* ATTACHMENT PICKER *********/
		initAttachmentButton();

		/******** BOTTOM MENU ***********/
		initBottomMenu();
	}

	public void setDefaultProfile() {
		currSubP = new SubProfile("", "", 10, 0xFF000000, 0xFFFF0000,
				0xFFFFFFFF, 0xFFFF0000, 600, false);
		currSubP.save = false;
		currSubP.saveAs = false;

		preSubP = null;

		reloadCustomize();
	}

	/**
	 * Initialize the style chooser buttons
	 */
	private void initStyleChoser() {
		hourglassButton = (Button) getActivity().findViewById(
				R.id.houglassButton);
		hourglassButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				selectStyle(formFactor.Hourglass);

			}
		});

		timetimerButton = (Button) getActivity().findViewById(
				R.id.timetimerButton);
		timetimerButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				selectStyle(formFactor.TimeTimer);

			}
		});

		progressbarButton = (Button) getActivity().findViewById(
				R.id.progressbarButton);
		progressbarButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				selectStyle(formFactor.ProgressBar);

			}
		});

		digitalButton = (Button) getActivity().findViewById(R.id.digitalButton);
		digitalButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				selectStyle(formFactor.DigitalClock);

			}
		});
	}

	/**
	 * Change style on currSubP according to formFactor type
	 * 
	 * @param formType
	 *            Style to change to
	 */
	private void selectStyle(formFactor formType) {
		if (formType == formFactor.Hourglass) {
			currSubP = currSubP.toHourglass();
			hourglassButton.setSelected(true);
			setSave();
		} else {
			hourglassButton.setSelected(false);
		}

		if (formType == formFactor.TimeTimer) {
			currSubP = currSubP.toTimeTimer();
			timetimerButton.setSelected(true);
			setSave();
		} else {
			timetimerButton.setSelected(false);
		}
		if (formType == formFactor.ProgressBar) {
			currSubP = currSubP.toProgressBar();
			progressbarButton.setSelected(true);
			setSave();
		} else {
			progressbarButton.setSelected(false);
		}
		if (formType == formFactor.DigitalClock) {
			currSubP = currSubP.toDigitalClock();
			digitalButton.setSelected(true);
			setSave();
		} else {
			digitalButton.setSelected(false);
		}
		genDescription();
		initBottomMenu();
	}

	private boolean setSave() {
		boolean complete = false;
		if (guard.getChild() != null) {
			currSubP.save = true;
		} else {
			currSubP.save = false;
		}
		currSubP.saveAs = true;

		return complete;
	}

	private int previousMins;
	private int previousSecs;

	/**
	 * Initialize the time picker wheel
	 */
	private void initTimePicker() {
		/* Create minute Wheel */
		mins = (WheelView) getActivity().findViewById(R.id.minPicker);
		mins.setViewAdapter(new NumericWheelAdapter(getActivity()
				.getApplicationContext(), 0, 60));
		mins.setCyclic(true);

		/* Create Seconds wheel */
		secs = (WheelView) getActivity().findViewById(R.id.secPicker);
		secs.setViewAdapter(new NumericWheelAdapter(getActivity()
				.getApplicationContext(), 0, 59));
		secs.setCyclic(true);

		/* Create description of time chosen */
		timeDescription = (TextView) getActivity().findViewById(R.id.showTime);
		setTime(currSubP.totalTime);

		/* Add on change listeners for both wheels */
		mins.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				updateTime(mins.getCurrentItem(), secs.getCurrentItem());

				if (mins.getCurrentItem() == 60) {
					previousMins = 60;
					previousSecs = secs.getCurrentItem();

					secs.setCurrentItem(0);
					secs.setViewAdapter(new NumericWheelAdapter(getActivity()
							.getApplicationContext(), 0, 0));
					secs.setCyclic(false);
				} else if (previousMins == 60) {
					secs.setViewAdapter(new NumericWheelAdapter(getActivity()
							.getApplicationContext(), 0, 60));

					secs.setCurrentItem(previousSecs);
					secs.setCyclic(true);
					previousMins = 0;
				}
			}
		});

		secs.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				updateTime(mins.getCurrentItem(), secs.getCurrentItem());
			}
		});
	}

	/**
	 * Sets the time on the time picker wheels
	 * 
	 * @param totalTime
	 *            Total time in seconds
	 */
	private void setTime(int totalTime) {
		int minutes, seconds;
		Log.e("Time", "" + totalTime);
		if (totalTime == 60) {
			minutes = 1;
			seconds = 0;
		} else if (totalTime >= 60 * 60) {
			minutes = 60;
			seconds = 0;
		} else {
			minutes = totalTime / 60;
			seconds = totalTime % 60;
		}

		mins.setCurrentItem(minutes);
		secs.setCurrentItem(seconds);

		if (minutes == 60) {
			previousMins = 60;
			previousSecs = seconds;

			secs.setCurrentItem(0);
			secs.setViewAdapter(new NumericWheelAdapter(getActivity()
					.getApplicationContext(), 0, 0));
			secs.setCyclic(false);
		} else if (previousMins == 60) {
			secs.setViewAdapter(new NumericWheelAdapter(getActivity()
					.getApplicationContext(), 0, 60));

			secs.setCurrentItem(previousSecs);
			secs.setCyclic(true);
			previousMins = 0;
		}

		updateTime(minutes, seconds);
	}

	/**
	 * Update time on currSubP and updates the time text
	 * 
	 * @param time
	 *            Total time in seconds
	 */
	private void updateTime(int m_minutes, int m_seconds) {
		currSubP.totalTime = (m_minutes * 60) + m_seconds;

		String timeText = "";
		/* F�rste del med mellemrum */
		if (m_minutes == 1) {
			timeText = "1 ";
			timeText += this.getString(R.string.minut);

		} else if (m_minutes != 0) {
			timeText = m_minutes + " ";
			timeText += this.getString(R.string.minutes);
		}

		/* Insert the devider if its needed */
		if (m_minutes != 0 && m_seconds != 0) {
			timeText += " " + this.getString(R.string.and_devider) + " ";
		}

		if (m_seconds == 1) {
			timeText += "1 ";
			timeText += this.getString(R.string.second);
		} else if (m_seconds != 0) {
			timeText += m_seconds + " ";
			timeText += this.getString(R.string.seconds);
		}

		timeDescription.setText(timeText);
		genDescription();
	}

	/**
	 * Initialize the color picker buttons, change colors here etc.
	 */
	private void initColorButtons() {
		checkboxGradient = (CheckBox) getActivity().findViewById(
				R.id.checkbox_gradient);

		checkboxGradient.setChecked(currSubP.gradient);
		checkboxGradient
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						currSubP.gradient = isChecked;
					}
				});

		colorGradientButton1 = (Button) getActivity().findViewById(
				R.id.gradientButton_1);

		setColor(colorGradientButton1.getBackground(), currSubP.timeLeftColor);

		// colorGradientButton1.setBackgroundColor(currSubP._timeLeftColor);
		colorGradientButton1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				AmbilWarnaDialog dialog = new AmbilWarnaDialog(getActivity(),
						currSubP.timeLeftColor, new OnAmbilWarnaListener() {
							public void onCancel(AmbilWarnaDialog dialog) {
							}

							public void onOk(AmbilWarnaDialog dialog, int color) {
								currSubP.timeLeftColor = color;
								setColor(colorGradientButton1.getBackground(),
										currSubP.timeLeftColor);
							}
						});
				dialog.show();
			}
		});

		colorGradientButton2 = (Button) getActivity().findViewById(
				R.id.gradientButton_2);
		setColor(colorGradientButton2.getBackground(), currSubP.timeSpentColor);
		colorGradientButton2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				AmbilWarnaDialog dialog = new AmbilWarnaDialog(getActivity(),
						currSubP.timeSpentColor, new OnAmbilWarnaListener() {
							public void onCancel(AmbilWarnaDialog dialog) {
							}

							public void onOk(AmbilWarnaDialog dialog, int color) {
								currSubP.timeSpentColor = color;
								setColor(colorGradientButton2.getBackground(),
										currSubP.timeSpentColor);
							}
						});
				dialog.show();
			}
		});

		colorFrameButton = (Button) getActivity().findViewById(
				R.id.frameColorButton);
		setColor(colorFrameButton.getBackground(), currSubP.frameColor);
		colorFrameButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				AmbilWarnaDialog dialog = new AmbilWarnaDialog(getActivity(),
						currSubP.frameColor, new OnAmbilWarnaListener() {
							public void onCancel(AmbilWarnaDialog dialog) {
							}

							public void onOk(AmbilWarnaDialog dialog, int color) {
								currSubP.frameColor = color;
								setColor(colorFrameButton.getBackground(),
										currSubP.frameColor);
							}
						});
				dialog.show();
			}
		});

		colorBackgroundButton = (Button) getActivity().findViewById(
				R.id.backgroundColorButton);
		setColor(colorBackgroundButton.getBackground(), currSubP.bgcolor);
		colorBackgroundButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				AmbilWarnaDialog dialog = new AmbilWarnaDialog(getActivity(),
						currSubP.bgcolor, new OnAmbilWarnaListener() {
							public void onCancel(AmbilWarnaDialog dialog) {
							}

							public void onOk(AmbilWarnaDialog dialog, int color) {
								currSubP.bgcolor = color;
								setColor(colorBackgroundButton.getBackground(),
										currSubP.bgcolor);
							}
						});
				dialog.show();
			}

		});
	}

	private void setColor(Drawable d, int color) {
		PorterDuffColorFilter filter = new PorterDuffColorFilter(color,
				PorterDuff.Mode.SRC_ATOP);
		d.setColorFilter(filter);

	}

	/**
	 * Initialize the attachment button
	 */
	private void initAttachmentButton() {

		attachmentButton = (Button) getActivity().findViewById(
				R.id.customize_attachment);
		attachmentButton.setOnClickListener( new OnClickListener() {

			public void onClick(View v) {
				List<String> values = new ArrayList<String>();
				final List<Child> children;
				children = guard.Children();
				
				for (Child c : children) {
					values.add(c.name);
				}
				
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle(getString(R.string.attachment_button_description));
				builder.setItems(values.toArray(new CharSequence[values.size()]), new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						List<String> values = new ArrayList<String>();
						final List<SubProfile> subProfiles;
						subProfiles = children.get(which).SubProfiles();
						
						for (SubProfile subProfile : subProfiles) {
							values.add(subProfile.name);
						}

						// Cast values to CharSequence and put it in the builder
						AlertDialog.Builder builder2 = new AlertDialog.Builder(
								getActivity());
						builder2.setTitle(getString(R.string.attachment_button_description));
						builder2.setItems(values.toArray(new CharSequence[values.size()]), new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog, int item) {
								setAttachment(subProfiles.get(item));
							}
						});
						AlertDialog alert = builder2.create();
						alert.show();
						
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
			}
		}
	);
	}

	/**
	 * Sets the attachment to subProfile, resets if subProfile == null
	 * 
	 * @param subProfile
	 *            SubProfile to be attached
	 */
	private void setAttachment(SubProfile subProfile) {
		int pictureRes;
		int textRes;
		String attachText = getActivity().getApplicationContext().getString(
				R.string.attached);

		TextView attView = (TextView) getActivity().findViewById(
				R.id.customize_attachment_text);

		if (subProfile != null) {

			currSubP.setAttachment(subProfile);

			switch (subProfile.formType()) {
			case Hourglass:
				pictureRes = R.drawable.thumbnail_hourglass;
				textRes = R.string.customize_hourglass_description;
				break;
			case DigitalClock:
				pictureRes = R.drawable.thumbnail_digital;
				textRes = R.string.customize_digital_description;
				break;
			case ProgressBar:
				pictureRes = R.drawable.thumbnail_progressbar;
				textRes = R.string.customize_progressbar_description;
				break;
			case TimeTimer:
				pictureRes = R.drawable.thumbnail_timetimer;
				textRes = R.string.customize_timetimer_description;
				break;
			default:
				pictureRes = R.drawable.thumbnail_attachment;
				textRes = R.string.attachment_button_description;
				attachText = "";
				break;
			}
		} else {
			pictureRes = R.drawable.thumbnail_attachment;
			textRes = R.string.attachment_button_description;

			attachText = "";
		}

		attachmentButton.setCompoundDrawablesWithIntrinsicBounds(0, pictureRes,
				0, 0);
		attachmentButton.setText(textRes);
		attView.setText(attachText);
	}

	private void initBottomMenu() {
		initSaveButton();
		initSaveAsButton();
		initStartButton();
	}

	/**
	 * Initialize the save button
	 */
	private void initSaveButton() {
		saveButton = (Button) getActivity().findViewById(R.id.customize_save);
		Drawable d;
		if (currSubP.save && !guard.getChild().getLock()
				&& guard.getChild() != null) {
			d = getResources().getDrawable(R.drawable.thumbnail_save);
			saveButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					getName();
					if (preSubP != null) {
						TimerLoader.subProfileID = currSubP.save(preSubP);
					} else {
						TimerLoader.subProfileID = guard.getChild().save(
								currSubP);
					}
					guard.publishList().get(TimerLoader.profilePosition)
							.select();

					SubProfileFragment spf = (SubProfileFragment) getFragmentManager()
							.findFragmentById(R.id.subprofileFragment);
					spf.reloadSubProfiles();
				}
			});
		} else {
			d = getResources().getDrawable(R.drawable.thumbnail_save_gray);
			saveButton.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					Toast t = Toast.makeText(getActivity(),
							getString(R.string.cant_save), 2000);
					t.show();
				}
			});
		}

		saveButton.setCompoundDrawablesWithIntrinsicBounds(null, d, null, null);
	}

	protected void getName() {
		String name = "";
		if (currSubP.name == "") {

			switch (currSubP.formType()) {
			case Hourglass:
				name += getString(R.string.customize_hourglass_description);
				break;

			case DigitalClock:
				name += getString(R.string.customize_digital_description);
				break;

			case ProgressBar:
				name += getString(R.string.customize_progressbar_description);
				break;

			case TimeTimer:
				name += getString(R.string.customize_timetimer_description);
				break;

			default:
				name += "";
				break;
			}
		} else {
			name = currSubP.name;
		}
		Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(getActivity().getString(R.string.save_button));
		final EditText et = new EditText(getActivity());
		et.setText(name);
		builder.setView(et);
		builder.setPositiveButton(R.string.save_button,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						currSubP.name = et.getText().toString();
						guard.publishList().get(TimerLoader.profilePosition)
								.select();

						SubProfileFragment spf = (SubProfileFragment) getFragmentManager()
								.findFragmentById(R.id.subprofileFragment);
						spf.reloadSubProfiles();

					}

				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	private void genDescription() {
		String name = "";
		String desc = "";

		switch (currSubP.formType()) {
		case Hourglass:
			name += getString(R.string.customize_hourglass_description);
			break;

		case DigitalClock:
			name += getString(R.string.customize_digital_description);
			break;

		case ProgressBar:
			name += getString(R.string.customize_progressbar_description);
			break;

		case TimeTimer:
			name += getString(R.string.customize_timetimer_description);
			break;

		default:
			name += "";
			break;
		}

		int seconds = currSubP.totalTime % 60;
		int minutes = (currSubP.totalTime - seconds) / 60;

		desc += name + " - ";
		desc += "(" + minutes + ":" + seconds + ")";

		currSubP.desc = desc;
	}

	/**
	 * Initialize the Save As button
	 */
	private void initSaveAsButton() {
		final ArrayList<String> values = new ArrayList<String>();
		Drawable d;
		for (Child c : guard.Children()) {
			values.add(c.name);
		}

		// Cast values to CharSequence
		final CharSequence[] items = values.toArray(new CharSequence[values
				.size()]);

		saveAsButton = (Button) getActivity().findViewById(
				R.id.customize_save_as);
		if (currSubP.saveAs) {
			d = getResources().getDrawable(R.drawable.thumbnail_saveas);
			saveAsButton.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							getActivity());
					builder.setTitle(getActivity().getString(
							R.string.choose_profile));
					builder.setItems(items,
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int item) {
									Child c = guard.Children().get(item);
									getName();
									c.save(currSubP);
									SubProfileFragment df = (SubProfileFragment) getFragmentManager()
											.findFragmentById(
													R.id.subprofileFragment);
									df.loadSubProfiles();

									String toastText = currSubP.name;
									toastText += " "
											+ getActivity()
													.getApplicationContext()
													.getText(
															R.string.toast_text);
									toastText += " " + c.name;

									Toast toast = Toast.makeText(getActivity(),
											toastText, 3000);
									toast.show();
								}
							});
					AlertDialog alert = builder.create();
					alert.show();
				}
			});
		} else {
			d = getResources().getDrawable(R.drawable.thumbnail_saveas_gray);
			saveAsButton.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					Toast t = Toast.makeText(getActivity(),
							getString(R.string.cant_save), 2000);
					t.show();
				}
			});
		}

		saveAsButton.setCompoundDrawablesWithIntrinsicBounds(null, d, null,
				null);

	}

	/**
	 * Initialize the start button
	 */
	private void initStartButton() {
		startButton = (Button) getActivity().findViewById(
				R.id.customize_start_button);
		Drawable d;
		if (currSubP.saveAs) {
			d = getResources().getDrawable(R.drawable.thumbnail_start);
			startButton.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					currSubP.addLastUsed(preSubP);
					currSubP.select();
					Intent i = new Intent(
							getActivity().getApplicationContext(),
							// TODO: Change according to openGL or canvas
							// OpenGLActivity.class);
							DrawActivity.class);
					startActivity(i);
				}
			});
		} else {
			d = getResources().getDrawable(R.drawable.thumbnail_start_gray);
			startButton.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					Toast t = Toast.makeText(getActivity(),
							getString(R.string.cant_start), 2000);
					t.show();
				}
			});
		}

		startButton
				.setCompoundDrawablesWithIntrinsicBounds(null, d, null, null);
	}

	/**
	 * Sets the predefined settings of the chosen subprofile
	 * 
	 * @param subp
	 *            The Subprofile chosen
	 */
	public void loadSettings(SubProfile subProfile) {
		// TODO: Insert logic to load settings and put them into the view
		currSubP = subProfile.copy();
		preSubP = subProfile;

		/* Set Style */
		selectStyle(currSubP.formType());

		/* Set Time */
		setTime(currSubP.totalTime);

		/* Set Colors */
		checkboxGradient.setChecked(currSubP.gradient);
		setColor(colorGradientButton1.getBackground(), currSubP.timeLeftColor);
		setColor(colorGradientButton2.getBackground(), currSubP.timeSpentColor);
		setColor(colorFrameButton.getBackground(), currSubP.frameColor);
		setColor(colorBackgroundButton.getBackground(), currSubP.bgcolor);

		/* Set Attachment */
		setAttachment(currSubP.getAttachment());
	}

	public void reloadCustomize() {
		/* Set Style */
		selectStyle(currSubP.formType());

		/* Set Time */
		setTime(currSubP.totalTime);

		/* Set Colors */
		checkboxGradient.setChecked(currSubP.gradient);
		setColor(colorGradientButton1.getBackground(), currSubP.timeLeftColor);
		setColor(colorGradientButton2.getBackground(), currSubP.timeSpentColor);
		setColor(colorFrameButton.getBackground(), currSubP.frameColor);
		setColor(colorBackgroundButton.getBackground(), currSubP.bgcolor);

		/* Set Attachment */
		setAttachment(currSubP.getAttachment());
	}
}
