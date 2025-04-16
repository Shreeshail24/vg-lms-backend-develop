package com.samsoft.lms.newux.analytics.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReportUserConfig {

    public boolean bookmarkVisible;
    public boolean fieldVisible;
    public boolean filterVisible;
    public boolean navigationVisible;
    public boolean selectionVisible;
    public boolean slicerVisible;
}
