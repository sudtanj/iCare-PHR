package sud_tanj.com.icare.Backend.Microcontrollers.CustomMicrocontroller;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

import com.google.gson.JsonParser;
import com.orhanobut.logger.Logger;
import com.potterhsu.usblistener.UsbListener;
import com.potterhsu.usblistener.UsbListener.OnUsbListener;

import java.nio.charset.Charset;

import cn.wch.ch34xuartdriver.CH34xUARTDriver;
import lombok.Setter;
import sud_tanj.com.icare.Backend.Microcontrollers.BaseMicrocontroller;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 08/08/2018 - 15:50.
 * <p>
 * This class last modified by User
 */
public class ArduinoUnoCH340 extends BaseMicrocontroller implements OnUsbListener {
    public static final String CH340_USB_PERMISSION="cn.wch.wchusbdriver.USB_PERMISSION",
            CH340_USB_STATE="android.hardware.usb.action.USB_STATE";
    private static ArduinoUnoCH340 arduinoUnoCH340=null;
    private CH34xUARTDriver ch34xUARTDriver;
    private UsbListener usbListener;
    private UsbDevice usbDevice;
    private JsonParser jsonParser;
    private Boolean usbAttached=false;
    private byte[] buffer=new byte[4096];
    private int bufferLength=0;
    @Setter
    private int baudRate = 9600;
    @Setter
    private byte stopBit = 1,
            dataBit = 8,
            parity = 0,
            flowControl = 0;

    public synchronized static ArduinoUnoCH340 getInstance(){
        if(arduinoUnoCH340==null)
            arduinoUnoCH340=new ArduinoUnoCH340();
        return arduinoUnoCH340;
    }

    public ArduinoUnoCH340() {
        super();
        usbListener = new UsbListener(getContext(), true,this);

        UsbManager usbManager=(UsbManager) getContext().getSystemService(Context.USB_SERVICE);
        ch34xUARTDriver=new CH34xUARTDriver(usbManager,getContext(),CH340_USB_PERMISSION);
        this.jsonParser=new JsonParser();
    }

    public void updateConfiguration(){
        if(this.usbDevice!=null)
            ch34xUARTDriver.SetConfig(baudRate, dataBit, stopBit, parity, flowControl);
    }

    @Override
    public void onAttached(UsbDevice usbDevice) {
        System.out.println("USbAttach");
        if(!ch34xUARTDriver.isConnected()){
            System.out.println("ch340 connnected");
            if(ch34xUARTDriver.ResumeUsbList()==0){
                System.out.println("UsbList Fopund");
                this.usbDevice=ch34xUARTDriver.EnumerateDevice();
                usbAttached=true;
                updateConfiguration();
            }
        }
    }

    @Override
    public void onDetached(UsbDevice usbDevice) {
        if(ch34xUARTDriver.isConnected()){
            if(ch34xUARTDriver.ResumeUsbList()==-1){
                this.usbDevice=null;
                usbAttached=false;
                ch34xUARTDriver.CloseDevice();
            }
        }
    }

    @Override
    public void onDispose() {
        super.onDispose();
        usbListener.dispose();
        ArduinoUnoCH340.arduinoUnoCH340=null;
    }

    @Override
    public void run() {
        if(usbAttached) {
            bufferLength = ch34xUARTDriver.ReadData(buffer, 4096);
            if (bufferLength > 0) {
                String recv = new String(buffer, 0, bufferLength, Charset.forName("US-ASCII"));
                Logger.i(this.toString(),recv);
                try {
                    fireEventListener(this.jsonParser.parse(recv).getAsJsonObject());
                } catch (Exception e){}
            }
        }
    }
}
