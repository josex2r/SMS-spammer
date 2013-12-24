package com.josex2R.sms_spammer;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	EditText phone, content;
	TextView amountText;
	Button send;
	SeekBar amountBar;
	int amount;
	SMS asyncSpammer=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		phone=(EditText)findViewById(R.id.phone);
		ImageButton btnContacts=(ImageButton)findViewById(R.id.btnContacts);
		btnContacts.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent myIntent=new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
				myIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
				startActivityForResult(myIntent, 0);
			}
		});
		
		content=(EditText)findViewById(R.id.content);
		
		amount=0;
		amountBar=(SeekBar)findViewById(R.id.amountBar);
		amountText=(TextView)findViewById(R.id.amountText);
		amountBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				amount=progress;
				amountText.setText(Integer.toString(amount));
			}
		});
		
		send=(Button)findViewById(R.id.send);
		ViewHolder.logger=(TextView)findViewById(R.id.loggerView);
		ViewHolder.progress=(ProgressBar)findViewById(R.id.progressBar);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK && requestCode==0){
			Uri contactUri=data.getData();
			String[] columnsProjection={ContactsContract.CommonDataKinds.Phone.NUMBER};
			Cursor myCursor=getContentResolver().query(contactUri, columnsProjection, null, null, null);
			myCursor.moveToFirst();
			int index =myCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
			phone.setText(myCursor.getString(index));
			myCursor.close();
		}else
			Toast.makeText(getApplicationContext(), "Se produjo un error inesperado", Toast.LENGTH_LONG);
	}
	
	public void cancelSMS(View view){
		if(asyncSpammer!=null && asyncSpammer.isExecuting)
			asyncSpammer.cancel(true);
		else
			ViewHolder.logger.setText("Ningún envío ejecutándose..");
	}

	public void sendSMS(View view){
		if(asyncSpammer!=null && asyncSpammer.isExecuting)
			asyncSpammer.cancel(true);
		else{
			String phoneNumber=phone.getText().toString();
			String textMessage=content.getText().toString();
			if(phoneNumber!="" && textMessage!="" && amount>0){
				asyncSpammer=new SMS(phoneNumber, textMessage, amount);
				asyncSpammer.execute();
			}else
				Toast.makeText(getApplicationContext(), "No ha completado todos los campos", Toast.LENGTH_LONG).show();
		}
	}

}
