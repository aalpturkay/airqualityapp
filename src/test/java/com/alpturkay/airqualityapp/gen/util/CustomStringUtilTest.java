package com.alpturkay.airqualityapp.gen.util;
import com.alpturkay.airqualityapp.gen.utils.CustomStringUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CustomStringUtilTest {

    @Test
    void shouldLowerAndCapitalizeFirstLetter(){
        String text = "MobIlEAcTioN";
        String expected = "Mobileaction";
        String actual = CustomStringUtil.lowerAndCapitalizeFirstLetter(text);
        assertEquals(expected, actual);
    }
}
