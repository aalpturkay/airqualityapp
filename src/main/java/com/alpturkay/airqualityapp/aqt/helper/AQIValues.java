package com.alpturkay.airqualityapp.aqt.helper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AQIValues {
    private final int goodIndex;
    private final int satisfactoryIndex;
    private final int moderateIndex;
    private final int poorIndex;
    private final int severeIndex;
    private final int hazardousIndex;

}
