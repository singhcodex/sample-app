package com.mycompany.myapp.repository.search;

import static org.springframework.data.elasticsearch.client.elc.QueryBuilders.queryStringQuery;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.mycompany.myapp.domain.Applicant;
import com.mycompany.myapp.repository.ApplicantRepository;
import java.util.List;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Spring Data Elasticsearch repository for the {@link Applicant} entity.
 */
public interface ApplicantSearchRepository extends ElasticsearchRepository<Applicant, Long>, ApplicantSearchRepositoryInternal {}

interface ApplicantSearchRepositoryInternal {
    Page<Applicant> search(String query, Pageable pageable);

    Page<Applicant> search(Query query);

    @Async
    void index(Applicant entity);

    @Async
    void deleteFromIndexById(Long id);
}

class ApplicantSearchRepositoryInternalImpl implements ApplicantSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final ApplicantRepository repository;

    ApplicantSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, ApplicantRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Applicant> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Applicant> search(Query query) {
        SearchHits<Applicant> searchHits = elasticsearchTemplate.search(query, Applicant.class);
        List<Applicant> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Applicant entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Applicant.class);
    }
}
