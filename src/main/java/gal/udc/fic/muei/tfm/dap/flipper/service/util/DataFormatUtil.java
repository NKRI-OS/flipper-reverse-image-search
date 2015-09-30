package gal.udc.fic.muei.tfm.dap.flipper.service.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * This file is part of Flipper Open Reverse Image Search.

 Flipper Open Reverse Image Search is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 Flipper Open Reverse Image Search is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Flipper Open Reverse Image Search.  If not, see <http://www.gnu.org/licenses/>.
 */
public class DataFormatUtil {


    /**
     * Join array using a formating Locale US for double as String
     * @param data
     * @param glue
     * @param formatString
     * @return
     */
    private static String joinArray(double[] data, String glue, String formatString) {

        StringBuilder buf = new StringBuilder(data.length * 2);

        for(int i = 0; i < data.length; ++i) {
            buf.append(String.format(Locale.US, formatString, new Object[]{Double.valueOf(data[i])}));
            if(i < data.length - 1) {
                buf.append(glue);
            }
        }

        return buf.toString();
    }

    /**
     * Serialize to byte array
     * @param features
     * @return
     * @throws IOException
     */
    public static byte[] serialize(List<double[]> features) throws IOException {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(bos);

        out.writeObject(features);
        byte[] result = bos.toByteArray();
        out.close();

        return result;
    }

    /**
     * Deserialize byte array to object List
     * @param features
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static List<Double> deserialize(byte[] features) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bis = new ByteArrayInputStream(features);
        ObjectInput in = new ObjectInputStream(bis);

        List<Double> result = (List<Double>) in.readObject();

        in.close();

        return result;

    }

    /**
     * Serialize double arrays list to list of strings
     * @param features
     * @return
     */
    public static List<String> serializeToString(List<double[]> features){

        List<String> result = new ArrayList<>();

        // convert the features to String with 5 decimal precision
        result.addAll(features.stream()
            .map(feature ->
                joinArray(feature, " ", "%.5f"))
            .collect(Collectors.toList()));

        return result;
    }

    /**
     * Deserialize list of strings to double arrays list
     * @param features
     * @return
     */
    public static List<double[]> deserializeFromString(List<String> features){

        List<double[]> result = new ArrayList<>();

        // convert the features to String with 5 decimal precision
        String[] array;
        double[] result2;
        for(String s : features){
            array = s.split(",");
            result2 = new double[s.length()];
            for(int i=0; i<array.length; i++){
                result2[i]=Double.parseDouble(array[i]);
            }
            result.add(result2);
        }

        return result;
    }

    /**
     * Seriale array to sorted list
     * @param features
     * @return
     */
    public static List<Double> serializeToDouble(List<double[]> features) {
        List<Double> result = new ArrayList<>();

        // convert the features to String with 5 decimal precision
        for(double[] doubles : features){
            for(double d : doubles){
                result.add(d);
            }
        }

        return result;
    }


    /**
     * Seriale array to sorted list
     * @param features
     * @return
     */
    public static List<Double> serializeFromArrayToDouble(double[] features) {
        List<Double> result = new ArrayList<>();

        // convert the features to String with 5 decimal precision
        for(double d : features){
            result.add(d);
        }

        return result;
    }
}
