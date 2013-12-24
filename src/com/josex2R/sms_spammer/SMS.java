package com.josex2R.sms_spammer;

import android.os.AsyncTask;
import android.telephony.SmsManager;
import android.widget.TextView;

public class SMS extends AsyncTask {
	
	private String number, text;
	private int amount;
	public boolean isExecuting;
	TextView logger;
	
	public SMS(String number, String text, int amount){
		this.number=number;
		this.text=text;
		this.amount=amount;
		this.isExecuting=false;
	}
	
	@Override
	protected Object doInBackground(Object... arg0) {
		if(this.isExecuting){
			//this.logger.setText("Cancelando envío");
			this.cancel(true);
			
		}else{
			//this.logger.setText("aaaaa");
			//MainActivity.updateLog("Iniciando envío");
			try{
				this.isExecuting=true;
				SmsManager sm = SmsManager.getDefault();
				for(int i=0;i<this.amount;i++){
					if (isCancelled())
						break;
					else{
						//System.out.println(i);
						//this.logger.setText("Enviando mensaje: "+i);
						sm.sendTextMessage(number, null, text, null, null);
						publishProgress(i);
					}
					Thread.sleep(500);	
				}
				
			}catch(InterruptedException ex){
			    Thread.currentThread().interrupt();
			    this.isExecuting=false;
			}
		}
		this.isExecuting=false;
		//this.logger.setText("Finalizando envío");
		return null;
	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		ViewHolder.progress.setProgress(0);
		ViewHolder.logger.setText("Iniciando envío...");
		super.onPreExecute();
	}
	
	@Override
	protected void onPostExecute(Object result) {
		// TODO Auto-generated method stub
		ViewHolder.progress.setProgress(100);
		ViewHolder.logger.setText("Envío finalizado...");
		super.onPostExecute(result);
	}
	
	@Override
	protected void onCancelled() {
		// TODO Auto-generated method stub
		ViewHolder.logger.setText("Envío cancelado...");
		super.onCancelled();
	}
	
	@Override
	protected void onProgressUpdate(Object... values) {
		// TODO Auto-generated method stub
		int progress=(int)(Integer.parseInt(values[0].toString())*100/this.amount);
		//ViewHolder.logger.setText("Enviando mensaje -> "+values[0]+"\nProgreso: "+Integer.toString(progress));
		ViewHolder.progress.setProgress(progress);
		super.onProgressUpdate(values);
	}

}
