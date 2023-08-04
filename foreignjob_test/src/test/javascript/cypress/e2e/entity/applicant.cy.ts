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

describe('Applicant e2e test', () => {
  const applicantPageUrl = '/applicant';
  const applicantPageUrlPattern = new RegExp('/applicant(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const applicantSample = {};

  let applicant;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/applicants+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/applicants').as('postEntityRequest');
    cy.intercept('DELETE', '/api/applicants/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (applicant) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/applicants/${applicant.id}`,
      }).then(() => {
        applicant = undefined;
      });
    }
  });

  it('Applicants menu should load Applicants page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('applicant');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Applicant').should('exist');
    cy.url().should('match', applicantPageUrlPattern);
  });

  describe('Applicant page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(applicantPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Applicant page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/applicant/new$'));
        cy.getEntityCreateUpdateHeading('Applicant');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', applicantPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/applicants',
          body: applicantSample,
        }).then(({ body }) => {
          applicant = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/applicants+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/applicants?page=0&size=20>; rel="last",<http://localhost/api/applicants?page=0&size=20>; rel="first"',
              },
              body: [applicant],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(applicantPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Applicant page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('applicant');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', applicantPageUrlPattern);
      });

      it('edit button click should load edit Applicant page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Applicant');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', applicantPageUrlPattern);
      });

      it('edit button click should load edit Applicant page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Applicant');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', applicantPageUrlPattern);
      });

      it('last delete button click should delete instance of Applicant', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('applicant').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', applicantPageUrlPattern);

        applicant = undefined;
      });
    });
  });

  describe('new Applicant page', () => {
    beforeEach(() => {
      cy.visit(`${applicantPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Applicant');
    });

    it('should create an instance of Applicant', () => {
      cy.get(`[data-cy="firstName"]`).type('Carole');
      cy.get(`[data-cy="firstName"]`).should('have.value', 'Carole');

      cy.get(`[data-cy="lastName"]`).type('O&#39;Reilly');
      cy.get(`[data-cy="lastName"]`).should('have.value', 'O&#39;Reilly');

      cy.get(`[data-cy="email"]`).type('Halie_Conn-Kuhn@hotmail.com');
      cy.get(`[data-cy="email"]`).should('have.value', 'Halie_Conn-Kuhn@hotmail.com');

      cy.get(`[data-cy="phoneNumber"]`).type('reboot');
      cy.get(`[data-cy="phoneNumber"]`).should('have.value', 'reboot');

      cy.get(`[data-cy="streetAddress"]`).type('Loan');
      cy.get(`[data-cy="streetAddress"]`).should('have.value', 'Loan');

      cy.get(`[data-cy="postalCode"]`).type('program');
      cy.get(`[data-cy="postalCode"]`).should('have.value', 'program');

      cy.get(`[data-cy="city"]`).type('Borerboro');
      cy.get(`[data-cy="city"]`).should('have.value', 'Borerboro');

      cy.get(`[data-cy="stateProvince"]`).type('Berkshire comedy input');
      cy.get(`[data-cy="stateProvince"]`).should('have.value', 'Berkshire comedy input');

      cy.get(`[data-cy="country"]`).type('Faroe Islands');
      cy.get(`[data-cy="country"]`).should('have.value', 'Faroe Islands');

      cy.get(`[data-cy="education"]`).type('teal generating');
      cy.get(`[data-cy="education"]`).should('have.value', 'teal generating');

      cy.get(`[data-cy="skills"]`).type('Implementation');
      cy.get(`[data-cy="skills"]`).should('have.value', 'Implementation');

      cy.setFieldImageAsBytesOfEntity('resume', 'integration-test.png', 'image/png');

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        applicant = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', applicantPageUrlPattern);
    });
  });
});
