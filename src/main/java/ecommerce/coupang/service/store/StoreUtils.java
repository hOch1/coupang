package ecommerce.coupang.service.store;

import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.common.exception.ErrorCode;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.store.Store;

public class StoreUtils {

    public static void validateStoreOwner(Store store, Member member) throws CustomException {
        if (!store.getMember().equals(member))
            throw new CustomException(ErrorCode.FORBIDDEN);
    }
}
