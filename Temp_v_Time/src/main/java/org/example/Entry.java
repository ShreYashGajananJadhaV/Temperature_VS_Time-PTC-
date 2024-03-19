package org.example;


import lombok.*;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "data")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Entry {
    @XmlElement(name = "Serial_Num")
    private int serialNum;

    @XmlElement(name = "Temperature")
    private float temperature;

    @XmlElement(name="Time")
    private int time;

}

