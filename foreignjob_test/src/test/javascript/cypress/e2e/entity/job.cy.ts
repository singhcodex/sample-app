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

describe('Job e2e test', () => {
  const jobPageUrl = '/job';
  const jobPageUrlPattern = new RegExp('/job(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const jobSample = {};

  let job;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/jobs+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/jobs').as('postEntityRequest');
    cy.intercept('DELETE', '/api/jobs/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (job) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/jobs/${job.id}`,
      }).then(() => {
        job = undefined;
      });
    }
  });

  it('Jobs menu should load Jobs page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('job');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Job').should('exist');
    cy.url().should('match', jobPageUrlPattern);
  });

  describe('Job page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(jobPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Job page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/job/new$'));
        cy.getEntityCreateUpdateHeading('Job');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', jobPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/jobs',
          body: jobSample,
        }).then(({ body }) => {
          job = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/jobs+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/jobs?page=0&size=20>; rel="last",<http://localhost/api/jobs?page=0&size=20>; rel="first"',
              },
              body: [job],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(jobPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Job page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('job');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', jobPageUrlPattern);
      });

      it('edit button click should load edit Job page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Job');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', jobPageUrlPattern);
      });

      it('edit button click should load edit Job page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Job');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', jobPageUrlPattern);
      });

      it('last delete button click should delete instance of Job', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('job').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', jobPageUrlPattern);

        job = undefined;
      });
    });
  });

  describe('new Job page', () => {
    beforeEach(() => {
      cy.visit(`${jobPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Job');
    });

    it('should create an instance of Job', () => {
      cy.get(`[data-cy="jobTitle"]`).type('Chief Creative Supervisor');
      cy.get(`[data-cy="jobTitle"]`).should('have.value', 'Chief Creative Supervisor');

      cy.get(`[data-cy="department"]`).type('lavender Gasoline');
      cy.get(`[data-cy="department"]`).should('have.value', 'lavender Gasoline');

      cy.get(`[data-cy="industry"]`).type('behind Androgyne');
      cy.get(`[data-cy="industry"]`).should('have.value', 'behind Androgyne');

      cy.get(`[data-cy="vacancies"]`).type('25764');
      cy.get(`[data-cy="vacancies"]`).should('have.value', '25764');

      cy.get(`[data-cy="expiryDate"]`).type('2023-08-04');
      cy.get(`[data-cy="expiryDate"]`).blur();
      cy.get(`[data-cy="expiryDate"]`).should('have.value', '2023-08-04');

      cy.get(`[data-cy="streetAddress"]`).type('Hop');
      cy.get(`[data-cy="streetAddress"]`).should('have.value', 'Hop');

      cy.get(`[data-cy="postalCode"]`).type('Gender reappear');
      cy.get(`[data-cy="postalCode"]`).should('have.value', 'Gender reappear');

      cy.get(`[data-cy="city"]`).type('New Columbus');
      cy.get(`[data-cy="city"]`).should('have.value', 'New Columbus');

      cy.get(`[data-cy="stateProvince"]`).type('parse website');
      cy.get(`[data-cy="stateProvince"]`).should('have.value', 'parse website');

      cy.get(`[data-cy="country"]`).type('Vanuatu');
      cy.get(`[data-cy="country"]`).should('have.value', 'Vanuatu');

      cy.get(`[data-cy="jobRequirement"]`).type('Cab neural Course');
      cy.get(`[data-cy="jobRequirement"]`).should('have.value', 'Cab neural Course');

      cy.get(`[data-cy="jobResponsibility"]`).type('virtual tesla female');
      cy.get(`[data-cy="jobResponsibility"]`).should('have.value', 'virtual tesla female');

      cy.get(`[data-cy="skills"]`).type('Chicken');
      cy.get(`[data-cy="skills"]`).should('have.value', 'Chicken');

      cy.get(`[data-cy="language"]`).select('FRENCH');

      cy.get(`[data-cy="minSalary"]`).type('8544');
      cy.get(`[data-cy="minSalary"]`).should('have.value', '8544');

      cy.get(`[data-cy="maxSalary"]`).type('4510');
      cy.get(`[data-cy="maxSalary"]`).should('have.value', '4510');

      cy.get(`[data-cy="workingHours"]`).type('2548');
      cy.get(`[data-cy="workingHours"]`).should('have.value', '2548');

      cy.get(`[data-cy="benefits"]`).type('but Analyst');
      cy.get(`[data-cy="benefits"]`).should('have.value', 'but Analyst');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        job = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', jobPageUrlPattern);
    });
  });
});
