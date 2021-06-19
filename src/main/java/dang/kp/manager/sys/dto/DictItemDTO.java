package dang.kp.manager.sys.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DictItemDTO {
    private String itemId;
    private String value;
    private String text;
    private Integer lineIndex;
    private String typeId;
    private String note;
}
