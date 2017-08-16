import { ReportsPage } from './app.po';

describe('reports App', () => {
  let page: ReportsPage;

  beforeEach(() => {
    page = new ReportsPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
