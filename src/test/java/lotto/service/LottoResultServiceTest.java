package lotto.service;

import lotto.domain.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class LottoResultServiceTest {
    private final WinningLotto winningLotto = new WinningLotto(new Lotto(List.of(1, 2, 3, 4, 5, 6)), 7);

    @ParameterizedTest
    @MethodSource("setLottoParam")
    @DisplayName("DTO ranks 전달 시 정상적으로 모두 전달되어야 한다.")
    void 로또_서비스_DTO_전달_정상(PurchasedLotto purchasedLotto) {
        //given
        LottoResultService service = new LottoResultService(winningLotto, purchasedLotto);
        //when
        Map<LottoRankInfo, Integer> ranks = new EnumMap<>(LottoRankInfo.class);
        purchasedLotto.lotto()
                .stream()
                .map(winningLotto::getLottoRank)
                .forEach(e -> ranks.merge(e, 1, Integer::sum));
        //then
        assertThat(service.createLottoResultDto()
                .getRanks().toString()).hasToString(ranks.toString());
    }

    @ParameterizedTest
    @MethodSource("setLottoParam")
    @DisplayName("DTO 수익률 전달 시 정상적으로 모두 전달되어야 한다.")
    void 로또_서비스_DTO_수익률_전달_정상(PurchasedLotto purchasedLotto) {
        //given
        LottoResultService service = new LottoResultService(winningLotto, purchasedLotto);
        Map<LottoRankInfo, Integer> ranks = new EnumMap<>(LottoRankInfo.class);
        //when
        purchasedLotto.lotto()
                .stream()
                .map(winningLotto::getLottoRank)
                .forEach(e -> ranks.merge(e, 1, Integer::sum));

        String returnRatio = new LottoResult(ranks).getReturnRatio();
        //then
        assertThat(service.createLottoResultDto().getReturnRatio()).hasToString(returnRatio);
    }

    static Stream<Arguments> setLottoParam() {
        return Stream.of(
                Arguments.of(new PurchasedLotto(List.of(new Lotto(List.of(1, 2, 3, 4, 5, 6))))),
                Arguments.of(new PurchasedLotto(List.of(new Lotto(List.of(1, 9, 3, 4, 5, 7))))),
                Arguments.of(new PurchasedLotto(List.of(new Lotto(List.of(1, 7, 3, 4, 5, 2))))),
                Arguments.of(new PurchasedLotto(List.of(new Lotto(List.of(4, 3, 2, 27, 5, 7))))),
                Arguments.of(new PurchasedLotto(List.of(new Lotto(List.of(10, 2, 13, 1, 5, 17))))),
                Arguments.of(new PurchasedLotto(List.of(new Lotto(List.of(1, 20, 31, 42, 5, 6))))),
                Arguments.of(new PurchasedLotto(List.of(new Lotto(List.of(10, 20, 31, 42, 12, 6)))))
        );
    }
}