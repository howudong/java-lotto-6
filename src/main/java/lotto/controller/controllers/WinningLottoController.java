package lotto.controller.controllers;

import lotto.domain.WinningLotto;
import lotto.dto.Dto;
import lotto.dto.WinningLottoInputDto;
import lotto.view.InputView;
import lotto.view.OutputView;
import lotto.view.ParameterConfig;

import java.util.Map;

public final class WinningLottoController implements Controller {
    private final InputView inputView;
    private final OutputView outputView;

    public WinningLottoController(InputView inputView, OutputView outputView) {
        this.inputView = inputView;
        this.outputView = outputView;
    }

    @Override
    public void process(Map<String, ? super Dto.Input> inputs, Map<String, ? super Dto.Output> outputs) {
        inputs.put(ParameterConfig.WINNING_LOTTO, new WinningLottoInputDto());
        inputLottoNumbers(inputs, outputs);
        inputBonusNumber(inputs, outputs);
    }

    private void inputLottoNumbers(Map<String, ? super Dto.Input> inputs, Map<String, ? super Dto.Output> outputs) {
        try {
            viewText(inputs, outputs, ParameterConfig.WINNING_LOTTO_NUMBERS);

        } catch (IllegalArgumentException e) {
            System.out.print(e.getMessage());
            inputLottoNumbers(inputs, outputs);
        }
    }

    private void viewText(Map<String, ? super Dto.Input> inputs, Map<String, ? super Dto.Output> outputs, String param) {
        outputs.put(param, null);
        outputView.view(outputs);
        inputView.read(inputs);

        outputs.remove(param);
    }

    private void inputBonusNumber(Map<String, ? super Dto.Input> inputs,
                                  Map<String, ? super Dto.Output> outputs) {
        try {
            viewText(inputs, outputs, ParameterConfig.BONUS_NUMBER);
            validateDuplication(inputs);
        } catch (IllegalArgumentException e) {
            System.out.print(e.getMessage());
            ((WinningLottoInputDto) inputs.get(ParameterConfig.WINNING_LOTTO)).setBonus(null);
            inputBonusNumber(inputs, outputs);
        }
    }

    private void validateDuplication(Map<String, ? super Dto.Input> inputs) {
        WinningLottoInputDto dto = (WinningLottoInputDto) inputs.get(ParameterConfig.WINNING_LOTTO);
        new WinningLotto(dto.getLotto(), dto.getBonus());
    }
}
