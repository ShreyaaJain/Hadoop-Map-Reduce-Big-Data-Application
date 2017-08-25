/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fp2;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableUtils;

/**
 *
 * @author sjain
 */
public class CompositeKeyWritable implements Writable, WritableComparable<CompositeKeyWritable>{
    
    private String flightNo;
    private String month;
    
    public CompositeKeyWritable(){
        
    }
    
    CompositeKeyWritable(String flightNo, String month){
        
        this.flightNo = flightNo;
        this.month = month;
        
    }

    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    
    
    

    @Override
    public void write(DataOutput d) throws IOException {
        WritableUtils.writeString(d, month);
        WritableUtils.writeString(d,flightNo);
        
    }

    @Override
    public void readFields(DataInput di) throws IOException {
        flightNo = WritableUtils.readString(di);
        month = WritableUtils.readString(di);
    }

    @Override
    public int compareTo(CompositeKeyWritable o) {
       int result = flightNo.compareTo(o.flightNo);
        if(result == 0)
        {
            result = month.compareTo(o.month);
        }
        return result;
    }
    
    @Override
    public String toString()
    {
        return (new StringBuilder().append(flightNo).append(":").append(month).append(",").toString());
    }
    
}
