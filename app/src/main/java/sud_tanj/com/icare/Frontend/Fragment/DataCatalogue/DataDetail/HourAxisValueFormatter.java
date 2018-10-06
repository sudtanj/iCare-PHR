package sud_tanj.com.icare.Frontend.Fragment.DataCatalogue.DataDetail;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Yasir on 02/06/16.
 */
public class HourAxisValueFormatter implements IAxisValueFormatter
{

    public static long referenceTimestamp=0; // minimum timestamp in your data set
    private DateFormat mDataFormat;
    private Date mDate;

    public HourAxisValueFormatter() {
        this.mDataFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        this.mDate = new Date();
    }


    /**
     * Called when a value from an axis is to be formatted
     * before being drawn. For performance reasons, avoid excessive calculations
     * and memory allocations inside this method.
     *
     * @param value the value to be formatted
     * @param axis  the axis the value belongs to
     * @return
     */
    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        // convertedTimestamp = originalTimestamp - referenceTimestamp
        int result=Arrays.binarySearch(axis.mEntries,value);
        if(result==(int)(axis.mEntryCount/2)
    || value==0 || result==axis.mEntryCount-1) {
            long convertedTimestamp = (long) value;

            // Retrieve original timestamp
            long originalTimestamp = referenceTimestamp + convertedTimestamp;
            SimpleDateFormat format2 = new SimpleDateFormat("MM/dd HH:mm:ss");
            axis.setGranularity(1f);

            return format2.format(new Date((originalTimestamp)));
        }
        return "";
        // Convert timestamp to hour:minute
        //return getHour(originalTimestamp);
    }


    private String getHour(long timestamp){
        try{
            mDate.setTime(timestamp*1000);
            return mDataFormat.format(mDate);
        }
        catch(Exception ex){
            return "xx";
        }
    }
}