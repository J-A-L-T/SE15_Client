package de.jalt.studeasy.client;

import com.jalt.studeasy.client.R;

/**
 * Auslagerung der Wahl der Feldfarbe für die Mainactivity im Hochformat.
 * @author Jan Mußenbrock und Lukas Erfkämper
 */
public class ColorChooser {

    static int getColorFromId(int subjectId){
    int color;
    switch (subjectId) {
        default:
            color = R.color.white;
            break;
        case 1:
            color = R.color.Red;
            break;
        case 2:
            color = R.color.Orange;
            break;
        case 3:
            color = R.color.Purple;
            break;
        case 4:
            color = R.color.Indigo;
            break;
        case 5:
            color = R.color.Light_Blue;
            break;
        case 6:
            color = R.color.Teal;
            break;
        case 7:
            color = R.color.Light_Green;
            break;
        case 8:
            color = R.color.Yellow;
            break;
        case 9:
            color = R.color.Brown;
            break;
        case 10:
            color = R.color.Green;
            break;  }
        return color;
    }

}
