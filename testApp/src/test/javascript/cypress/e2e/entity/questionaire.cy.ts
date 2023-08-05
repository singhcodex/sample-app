import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Questionaire e2e test', () => {
  const questionairePageUrl = '/questionaire';
  const questionairePageUrlPattern = new RegExp('/questionaire(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const questionaireSample = {};

  let questionaire;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/questionaires+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/questionaires').as('postEntityRequest');
    cy.intercept('DELETE', '/api/questionaires/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (questionaire) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/questionaires/${questionaire.id}`,
      }).then(() => {
        questionaire = undefined;
      });
    }
  });

  it('Questionaires menu should load Questionaires page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('questionaire');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Questionaire').should('exist');
    cy.url().should('match', questionairePageUrlPattern);
  });

  describe('Questionaire page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(questionairePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Questionaire page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/questionaire/new$'));
        cy.getEntityCreateUpdateHeading('Questionaire');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', questionairePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/questionaires',
          body: questionaireSample,
        }).then(({ body }) => {
          questionaire = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/questionaires+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [questionaire],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(questionairePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Questionaire page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('questionaire');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', questionairePageUrlPattern);
      });

      it('edit button click should load edit Questionaire page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Questionaire');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', questionairePageUrlPattern);
      });

      it('edit button click should load edit Questionaire page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Questionaire');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', questionairePageUrlPattern);
      });

      it('last delete button click should delete instance of Questionaire', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('questionaire').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', questionairePageUrlPattern);

        questionaire = undefined;
      });
    });
  });

  describe('new Questionaire page', () => {
    beforeEach(() => {
      cy.visit(`${questionairePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Questionaire');
    });

    it('should create an instance of Questionaire', () => {
      cy.get(`[data-cy="questTitle"]`).type('Cis Response');
      cy.get(`[data-cy="questTitle"]`).should('have.value', 'Cis Response');

      cy.get(`[data-cy="options"]`).type('Facilitator');
      cy.get(`[data-cy="options"]`).should('have.value', 'Facilitator');

      cy.get(`[data-cy="correctOption"]`).type('oof Licensed East');
      cy.get(`[data-cy="correctOption"]`).should('have.value', 'oof Licensed East');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        questionaire = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', questionairePageUrlPattern);
    });
  });
});
