package ecommerce.coupang.common.utils.store;

import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.common.exception.ErrorCode;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.store.Store;

public class StoreUtils {

    /*
     해당 상점의 주인인지 검증
     주인이 아닐경우 예외
     */
    public static void validateStoreOwner(Store store, Member member) throws CustomException {
        if (!store.getMember().equals(member))
            throw new CustomException(ErrorCode.FORBIDDEN);
    }
}
