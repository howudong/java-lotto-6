package lotto.view;

import lotto.dto.DTO;

import java.util.Map;

public interface OutputView {
    void view(Map<String, ? super DTO.Output> model);
}
