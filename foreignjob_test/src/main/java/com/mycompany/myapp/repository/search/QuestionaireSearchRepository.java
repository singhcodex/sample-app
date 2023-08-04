package com.mycompany.myapp.repository.search;

import static org.springframework.data.elasticsearch.client.elc.QueryBuilders.queryStringQuery;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.mycompany.myapp.domain.Questionaire;
import com.mycompany.myapp.repository.QuestionaireRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Spring Data Elasticsearch repository for the {@link Questionaire} entity.
 */
public interface QuestionaireSearchRepository extends ElasticsearchRepository<Questionaire, Long>, QuestionaireSearchRepositoryInternal {}

interface QuestionaireSearchRepositoryInternal {
    Stream<Questionaire> search(String query);

    Stream<Questionaire> search(Query query);

    @Async
    void index(Questionaire entity);

    @Async
    void deleteFromIndexById(Long id);
}

class QuestionaireSearchRepositoryInternalImpl implements QuestionaireSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final QuestionaireRepository repository;

    QuestionaireSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, QuestionaireRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<Questionaire> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<Questionaire> search(Query query) {
        return elasticsearchTemplate.search(query, Questionaire.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(Questionaire entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Questionaire.class);
    }
}
