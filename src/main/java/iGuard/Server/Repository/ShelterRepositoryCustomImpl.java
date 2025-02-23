package iGuard.Server.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import iGuard.Server.Dto.user.ShelterSearchDto;
import iGuard.Server.Entity.Shelter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static iGuard.Server.Entity.QShelter.shelter;

@Repository
@RequiredArgsConstructor
public class ShelterRepositoryCustomImpl implements ShelterRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Shelter> search(ShelterSearchDto dto, Pageable pageable) {

        // 필터 및 정렬 적용
        BooleanBuilder builder = applyFilters(dto);
        OrderSpecifier<?> orderSpecifier = applySorting(dto);

        JPQLQuery<Shelter> query = queryFactory
                .selectFrom(shelter)
                .where(builder)
                .orderBy(orderSpecifier);

        // 페이징 적용
        List<Shelter> content = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 개수 조회
        Long total = queryFactory
                .select(shelter.count())
                .from(shelter)
                .where(builder)
                .fetchOne();

        total = (total != null) ? total : 0;

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanBuilder applyFilters(ShelterSearchDto dto) {
        BooleanBuilder builder = new BooleanBuilder();

        if(StringUtils.hasText(dto.getCity())) {
            builder.and(shelter.address.like(dto.getCity()+"%"));
        }
        if(StringUtils.hasText(dto.getGu())) {
            builder.and(shelter.address.like(dto.getCity()+"%"+dto.getGu()+"%"));
        }
        if (StringUtils.hasText(dto.getShelterName())) {
            builder.and(shelter.shelterName.contains(dto.getShelterName()));
        }
        if (StringUtils.hasText(dto.getFacilityType())) {
            builder.and(shelter.facilityTypeName.eq(dto.getFacilityType()));
        }

        BooleanBuilder orBuilder = new BooleanBuilder();
        if (dto.getIsOpenAtNight() != null) {
            orBuilder.or(shelter.isOpenAtNight.eq(dto.getIsOpenAtNight()));
        }
        if (dto.getIsOpenOnHolidays() != null) {
            orBuilder.or(shelter.isOpenOnHolidays.eq(dto.getIsOpenOnHolidays()));
        }
        if (dto.getAllowsAccommodation() != null) {
            orBuilder.or(shelter.allowsAccommodation.eq(dto.getAllowsAccommodation()));
        }

        if (orBuilder.hasValue()) {
            builder.and(orBuilder);
        }

        return builder;
    }

    private OrderSpecifier<?> applySorting(ShelterSearchDto dto) {
        return switch (dto.getSortBy() != null ? dto.getSortBy() : "name") {
            case "name" -> shelter.shelterName.asc();
            case "currentUsage" -> shelter.currentOccupancy.asc();
            default -> shelter.shelterName.asc();
        };
    }

}
